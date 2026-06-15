package cn.yanque.studentFront.service.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.mapper.StudentMapper;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.models.teaching.homework.mapper.HomeworkMapper;
import cn.yanque.models.teaching.homework.pojo.entity.HomeworkEntity;
import cn.yanque.models.upload.pojo.vo.res.DownloadPresignRes;
import cn.yanque.models.upload.service.UploadService;
import cn.yanque.studentFront.pojo.req.StudentHomeworkPageReq;
import cn.yanque.studentFront.pojo.res.StudentHomeworkDownloadUrlRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkPageRes;
import cn.yanque.studentFront.service.StudentHomeworkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    private static final String DOWNLOAD_TYPE_CONTENT = "CONTENT";

    private static final String DOWNLOAD_TYPE_ANSWER = "ANSWER";

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

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
        List<StudentHomeworkPageRes> records = list.stream().map(this::buildHomeworkRes).toList();
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
        DownloadPresignRes presign = uploadService.createDownloadPresign(objectKey);
        StudentHomeworkDownloadUrlRes res = new StudentHomeworkDownloadUrlRes();
        res.setFileName(fileName);
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

    private StudentHomeworkPageRes buildHomeworkRes(HomeworkEntity homework) {
        StudentHomeworkPageRes res = new StudentHomeworkPageRes();
        BeanUtils.copyProperties(homework, res);
        boolean answerVisible = Boolean.TRUE.equals(homework.getAnswerStudentVisible()) && !isBlank(homework.getAnswerObjectKey());
        res.setAnswerVisible(answerVisible);
        if (!answerVisible) {
            res.setAnswerObjectKey(null);
            res.setAnswerFileName(null);
        }
        return res;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
