package cn.yanque.studentFront.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.studentFront.pojo.req.StudentExamPageReq;
import cn.yanque.studentFront.pojo.req.StudentExamSubmitReq;
import cn.yanque.studentFront.pojo.res.StudentExamPageRes;
import cn.yanque.studentFront.pojo.res.StudentExamPaperRes;
import cn.yanque.studentFront.pojo.res.StudentExamStartRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmissionRes;
import cn.yanque.studentFront.pojo.res.StudentExamSubmitRes;

public interface StudentExamService {

    PageResult<StudentExamPageRes> pageExam(StudentExamPageReq req);

    StudentExamStartRes startExam(Long examId);

    StudentExamPaperRes getExamPaper(Long recordId);

    StudentExamSubmitRes submitExam(Long recordId, StudentExamSubmitReq req);

    StudentExamSubmissionRes getSubmission(Long recordId);
}
