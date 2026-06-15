package cn.yanque.studentFront.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.studentFront.pojo.req.StudentHomeworkPageReq;
import cn.yanque.studentFront.pojo.res.StudentHomeworkDownloadUrlRes;
import cn.yanque.studentFront.pojo.res.StudentHomeworkPageRes;

public interface StudentHomeworkService {

    PageResult<StudentHomeworkPageRes> pageHomework(StudentHomeworkPageReq req);

    StudentHomeworkDownloadUrlRes createDownloadUrl(Long homeworkId, String type);
}
