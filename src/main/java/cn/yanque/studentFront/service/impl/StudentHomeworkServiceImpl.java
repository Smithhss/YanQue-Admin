package cn.yanque.studentFront.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.teaching.homework.mapper.HomeworkMapper;
import cn.yanque.models.teaching.homework.mapper.HomeworkSubmissionMapper;
import cn.yanque.models.teaching.homework.pojo.entity.HomeworkEntity;
import cn.yanque.models.teaching.homework.pojo.entity.HomeworkSubmissionEntity;
import cn.yanque.models.upload.pojo.vo.res.DownloadPresignRes;
import cn.yanque.models.upload.service.UploadService;
import cn.yanque.studentFront.pojo.req.StudentHomeworkPageReq;
import cn.yanque.studentFront.pojo.req.StudentHomeworkSubmitReq;
import cn.yanque.studentFront.pojo.res.StudentHomeworkDownloadUrlRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkPageRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkSubmitRes;
import cn.yanque.studentFront.service.StudentHomeworkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    private static final String DOWNLOAD_TYPE_CONTENT = "CONTENT";

    private static final String DOWNLOAD_TYPE_ANSWER = "ANSWER";

    private static final String HOMEWORK_SUBMISSION_PREFIX = "homework/submission/";

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkSubmissionMapper homeworkSubmissionMapper;

    @Autowired
    private UploadService uploadService;

    @Override
    public PageResult<StudentHomeworkPageRes> pageHomework( StudentHomeworkPageReq req) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        if (student.getClassId() == null) {
            return new PageResult<>(0L, req.getPageNum(), req.getPageSize(), List.of());
        }

        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<HomeworkEntity> list = homeworkMapper.selectStudentPage(student.getClassId(), new Date());
        PageInfo<HomeworkEntity> pageInfo = new PageInfo<>(list);
        // 作业分页以homework为主表，提交状态按当前学生批量补齐，避免分页SQL做不必要的关联。
        Map<Long, HomeworkSubmissionEntity> submissionMap = buildSubmissionMap(list, student.getId());
        List<StudentHomeworkPageRes> records = list.stream().map(item -> buildHomeworkRes(item, submissionMap.get(item.getId()))).toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    @Override
    public StudentHomeworkDownloadUrlRes createDownloadUrl(Long homeworkId, String type) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        HomeworkEntity homework = validateStudentHomework(student, homeworkId);
        String objectKey;
        String fileName;

        if (DOWNLOAD_TYPE_CONTENT.equalsIgnoreCase(type)) {
            objectKey = homework.getContentObjectKey();
            fileName = homework.getContentFileName();
        } else if (DOWNLOAD_TYPE_ANSWER.equalsIgnoreCase(type)) {
            if (!Boolean.TRUE.equals(homework.getAnswerStudentVisible())) {
                throw BusinessException.DateError.newInstance("答案暂未公布");
            }
            objectKey = homework.getAnswerObjectKey();
            fileName = homework.getAnswerFileName();
        } else {
            throw BusinessException.DateError.newInstance("下载类型错误");
        }

        if (isBlank(objectKey)) {
            throw BusinessException.DateError.newInstance("文件不存在");
        }
        // 下载签名统一交给upload模块生成，这里只负责学生作业的班级、开始时间、答案可见性校验。
        DownloadPresignRes presign = uploadService.createDownloadPresign(objectKey);
        StudentHomeworkDownloadUrlRes res = new StudentHomeworkDownloadUrlRes();
        res.setFileName(fileName);
        res.setDownloadUrl(presign.getDownloadUrl());
        res.setExpires(presign.getExpires());
        return res;
    }

    @Override
    public StudentHomeworkSubmitRes submitHomework(Long homeworkId, StudentHomeworkSubmitReq req) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        HomeworkEntity homework = validateStudentHomework(student, homeworkId);
        Date now = new Date();
        // 截止时间是提交硬边界，超过后不允许首次提交，也不允许重新提交覆盖旧文件。
        if (homework.getDeadline() != null && now.after(homework.getDeadline())) {
            throw BusinessException.DateError.newInstance("作业已截止，不能提交");
        }

        validateSubmissionObjectKey(req.getObjectKey(), homeworkId, student.getId());

        HomeworkSubmissionEntity submission = homeworkSubmissionMapper.selectByHomeworkIdAndStudentId(homeworkId, student.getId());
        if (submission == null) {
            submission = new HomeworkSubmissionEntity();
            submission.setHomeworkId(homeworkId);
            submission.setStudentId(student.getId());
            submission.setClassId(homework.getClassId());
            submission.setCreatedAt(now);
        }
        submission.setContentObjectKey(req.getObjectKey().trim());
        submission.setContentFileName(req.getFileName().trim());
        submission.setSubmitTime(now);
        submission.setLateSubmitted(false);
        submission.setUpdatedAt(now);

        // 一个学生对一份作业只保留一条提交记录，重新提交时覆盖文件信息和提交时间。
        if (submission.getId() == null) {
            homeworkSubmissionMapper.insert(submission);
        } else {
            homeworkSubmissionMapper.updateSubmit(submission);
        }

        StudentHomeworkSubmitRes res = new StudentHomeworkSubmitRes();
        res.setId(submission.getId());
        res.setHomeworkId(homeworkId);
        res.setContentFileName(submission.getContentFileName());
        res.setSubmitTime(submission.getSubmitTime());
        res.setLateSubmitted(submission.getLateSubmitted());
        return res;
    }

    @Override
    public StudentHomeworkDownloadUrlRes createSubmissionDownloadUrl(Long homeworkId) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        validateStudentHomework(student, homeworkId);
        HomeworkSubmissionEntity submission = homeworkSubmissionMapper.selectByHomeworkIdAndStudentId(homeworkId, student.getId());
        if (submission == null || isBlank(submission.getContentObjectKey())) {
            throw BusinessException.DateError.newInstance("尚未提交作业");
        }
        // 学生只能下载自己的提交记录，不能通过通用objectKey下载接口绕过归属校验。
        DownloadPresignRes presign = uploadService.createDownloadPresign(submission.getContentObjectKey());
        StudentHomeworkDownloadUrlRes res = new StudentHomeworkDownloadUrlRes();
        res.setFileName(submission.getContentFileName());
        res.setDownloadUrl(presign.getDownloadUrl());
        res.setExpires(presign.getExpires());
        return res;
    }

    private StudentEntity validateStudent(Long studentId) {
        StudentEntity student = studentMapper.selectById(studentId);
        if (student == null) {
            throw BusinessException.DateError.newInstance("学生不存在");
        }
        return student;
    }

    private HomeworkEntity validateStudentHomework(StudentEntity student, Long homeworkId) {
        HomeworkEntity homework = homeworkMapper.selectById(homeworkId);
        if (homework == null || student.getClassId() == null || !student.getClassId().equals(homework.getClassId())) {
            throw BusinessException.DateError.newInstance("作业不存在");
        }
        if (homework.getStartTime() == null || homework.getStartTime().after(new Date())) {
            throw BusinessException.DateError.newInstance("作业暂未开始");
        }
        return homework;
    }

    private Map<Long, HomeworkSubmissionEntity> buildSubmissionMap(List<HomeworkEntity> homeworks, Long studentId) {
        List<Long> homeworkIds = homeworks.stream().map(HomeworkEntity::getId).toList();
        if (homeworkIds.isEmpty()) {
            return Map.of();
        }
        // 当前接口只展示“我的提交状态”，所以按当前学生维度批量查询即可。
        return homeworkSubmissionMapper.selectByHomeworkIdsAndStudentId(homeworkIds, studentId).stream()
                .collect(Collectors.toMap(HomeworkSubmissionEntity::getHomeworkId, item -> item));
    }

    private StudentHomeworkPageRes buildHomeworkRes(HomeworkEntity homework, HomeworkSubmissionEntity submission) {
        StudentHomeworkPageRes res = new StudentHomeworkPageRes();
        BeanUtils.copyProperties(homework, res);
        boolean answerVisible = Boolean.TRUE.equals(homework.getAnswerStudentVisible()) && !isBlank(homework.getAnswerObjectKey());
        res.setAnswerVisible(answerVisible);
        if (!answerVisible) {
            // 答案未公布时不向学生端透出答案objectKey和文件名，避免前端误用或泄露。
            res.setAnswerObjectKey(null);
            res.setAnswerFileName(null);
        }
        boolean submitted = submission != null && !isBlank(submission.getContentObjectKey());
        res.setSubmitted(submitted);
        if (submitted) {
            res.setSubmissionFileName(submission.getContentFileName());
            res.setSubmitTime(submission.getSubmitTime());
            res.setLateSubmitted(submission.getLateSubmitted());
            res.setScore(submission.getScore());
            res.setTeacherRemark(submission.getTeacherRemark());
        }
        return res;
    }

    private void validateSubmissionObjectKey(String objectKey, Long homeworkId, Long studentId) {
        if (isBlank(objectKey) || !objectKey.trim().toLowerCase().endsWith(".md")) {
            throw BusinessException.DateError.newInstance("提交文件只支持md格式");
        }
        String normalizedObjectKey = objectKey.trim();
        String expectedPrefix = HOMEWORK_SUBMISSION_PREFIX + homeworkId + "/" + studentId + "/";
        // 学生提交文件必须落在自己的作业目录，避免用别人的objectKey覆盖提交记录。
        if (normalizedObjectKey.startsWith("/") || normalizedObjectKey.contains("..") || !normalizedObjectKey.startsWith(expectedPrefix)) {
            throw BusinessException.DateError.newInstance("提交文件路径不合法");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
