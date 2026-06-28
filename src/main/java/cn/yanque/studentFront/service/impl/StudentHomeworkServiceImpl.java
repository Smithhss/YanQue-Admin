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

/**
 * 学生端作业服务实现。
 *
 * <p>核心功能:
 * <ul>
 *   <li>查看作业列表(带提交状态和答案可见性)
 *   <li>下载作业文件/答案(权限校验)
 *   <li>提交作业(路径校验,截止时间校验,允许覆盖提交)
 *   <li>下载自己的提交记录
 * </ul>
 *
 * <p>数据权限:
 * <ul>
 *   <li>学生只能看到自己班级的作业
 *   <li>答案需要教师设置为可见后才能下载
 *   <li>学生只能下载自己的提交记录
 * </ul>
 */
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

    /**
     * 分页查询作业列表(学生端)。
     * 未分配班级的学生返回空列表,已分配班级的学生只能看到本班级的作业。
     */
    @Override
    public PageResult<StudentHomeworkPageRes> pageHomework(StudentHomeworkPageReq req) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        if (student.getClassId() == null) {
            return new PageResult<>(0L, req.getPageNum(), req.getPageSize(), List.of());
        }

        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null ? 10 : req.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<HomeworkEntity> list = homeworkMapper.selectStudentPage(student.getClassId(), new Date());
        PageInfo<HomeworkEntity> pageInfo = new PageInfo<>(list);

        // 批量查询当前学生的提交状态
        Map<Long, HomeworkSubmissionEntity> submissionMap = buildSubmissionMap(list, student.getId());
        List<StudentHomeworkPageRes> records = list.stream()
                .map(item -> buildHomeworkRes(item, submissionMap.get(item.getId())))
                .toList();
        return new PageResult<>(pageInfo.getTotal(), pageNum, pageSize, records);
    }

    /**
     * 生成作业文件/答案的下载 URL。
     * 答案需要教师设置为可见后才能下载。
     */
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

        DownloadPresignRes presign = uploadService.createDownloadPresign(objectKey);
        StudentHomeworkDownloadUrlRes res = new StudentHomeworkDownloadUrlRes();
        res.setFileName(fileName);
        res.setDownloadUrl(presign.getDownloadUrl());
        res.setExpires(presign.getExpires());
        return res;
    }

    /**
     * 提交作业。
     * 允许重新提交覆盖旧文件,但截止时间后不允许提交。
     */
    @Override
    public StudentHomeworkSubmitRes submitHomework(Long homeworkId, StudentHomeworkSubmitReq req) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        HomeworkEntity homework = validateStudentHomework(student, homeworkId);
        Date now = new Date();

        // 校验截止时间
        if (homework.getDeadline() != null && now.after(homework.getDeadline())) {
            throw BusinessException.DateError.newInstance("作业已截止,不能提交");
        }

        // 校验文件路径安全性
        validateSubmissionObjectKey(req.getObjectKey(), homeworkId, student.getId());

        // 查询或创建提交记录
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

        // 插入或更新
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

    /**
     * 生成学生提交记录的下载 URL。
     * 学生只能下载自己的提交记录。
     */
    @Override
    public StudentHomeworkDownloadUrlRes createSubmissionDownloadUrl(Long homeworkId) {
        StudentEntity student = validateStudent(StudentThreadLocal.get().getId());
        validateStudentHomework(student, homeworkId);
        HomeworkSubmissionEntity submission = homeworkSubmissionMapper.selectByHomeworkIdAndStudentId(homeworkId, student.getId());
        if (submission == null || isBlank(submission.getContentObjectKey())) {
            throw BusinessException.DateError.newInstance("尚未提交作业");
        }

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
        return homeworkSubmissionMapper.selectByHomeworkIdsAndStudentId(homeworkIds, studentId).stream()
                .collect(Collectors.toMap(HomeworkSubmissionEntity::getHomeworkId, item -> item));
    }

    private StudentHomeworkPageRes buildHomeworkRes(HomeworkEntity homework, HomeworkSubmissionEntity submission) {
        StudentHomeworkPageRes res = new StudentHomeworkPageRes();
        BeanUtils.copyProperties(homework, res);

        // 答案可见性控制
        boolean answerVisible = Boolean.TRUE.equals(homework.getAnswerStudentVisible()) && !isBlank(homework.getAnswerObjectKey());
        res.setAnswerVisible(answerVisible);
        if (!answerVisible) {
            res.setAnswerObjectKey(null);
            res.setAnswerFileName(null);
        }

        // 提交状态填充
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
        if (normalizedObjectKey.startsWith("/") || normalizedObjectKey.contains("..") || !normalizedObjectKey.startsWith(expectedPrefix)) {
            throw BusinessException.DateError.newInstance("提交文件路径不合法");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
