package cn.yanque.models.exam.exam.biz;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamPageReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSaveReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeReq;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionPageReq;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDeleteRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamPageRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSaveRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionGradeRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionPageRes;

/**
 * 考试安排业务编排。
 */
public interface ExamBiz {

    ExamSaveRes createExam(ExamSaveReq req);

    ExamSaveRes updateExam(ExamSaveReq req);

    PageResult<ExamPageRes> pageExam(ExamPageReq req);

    ExamDetailRes getExamById(Long id);

    ExamDeleteRes deleteExam(Long id);

    ExamSaveRes updateAnswerVisible(Long id, Boolean answerVisible);

    PageResult<ExamSubmissionPageRes> pageSubmissions(Long examId, ExamSubmissionPageReq req);

    ExamSubmissionDetailRes getSubmissionDetail(Long recordId);

    ExamSubmissionGradeRes gradeSubmission(Long recordId, ExamSubmissionGradeReq req);
}
