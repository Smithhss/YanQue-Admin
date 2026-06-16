package cn.yanque.models.exam.exam.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.exam.pojo.bo.QueryExamBo;
import cn.yanque.models.exam.exam.pojo.entity.ExamEntity;
import cn.yanque.models.exam.exam.pojo.vo.req.ExamSubmissionGradeReq;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamPageRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionDetailRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionGradeRes;
import cn.yanque.models.exam.exam.pojo.vo.res.ExamSubmissionPageRes;

/**
 * 考试安排领域服务。
 */
public interface ExamService {

    Long createExam(ExamEntity exam);

    Long updateExam(ExamEntity exam);

    PageResult<ExamPageRes> pageExam(QueryExamBo query, Integer pageNum, Integer pageSize);

    ExamDetailRes getExamById(Long id);

    Long deleteExam(Long id);

    Long updateAnswerVisible(Long id, Boolean answerVisible);

    PageResult<ExamSubmissionPageRes> pageSubmissions(Long examId, Integer pageNum, Integer pageSize);

    ExamSubmissionDetailRes getSubmissionDetail(Long recordId);

    ExamSubmissionGradeRes gradeSubmission(Long recordId, ExamSubmissionGradeReq req);
}
