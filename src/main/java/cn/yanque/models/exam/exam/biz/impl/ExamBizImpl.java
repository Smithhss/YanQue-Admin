package cn.yanque.models.exam.exam.biz.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.exam.biz.ExamBiz;
import cn.yanque.models.exam.exam.pojo.bo.QueryExamBo;
import cn.yanque.models.exam.exam.pojo.entity.ExamEntity;
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
import cn.yanque.models.exam.exam.service.ExamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamBizImpl implements ExamBiz {

    @Autowired
    private ExamService examService;

    @Override
    public ExamSaveRes createExam(ExamSaveReq req) {
        ExamSaveRes res = new ExamSaveRes();
        res.setId(examService.createExam(buildExam(req)));
        return res;
    }

    @Override
    public ExamSaveRes updateExam(ExamSaveReq req) {
        ExamSaveRes res = new ExamSaveRes();
        res.setId(examService.updateExam(buildExam(req)));
        return res;
    }

    @Override
    public PageResult<ExamPageRes> pageExam(ExamPageReq req) {
        QueryExamBo query = new QueryExamBo();
        BeanUtils.copyProperties(req, query);
        return examService.pageExam(query, req.getPageNum(), req.getPageSize());
    }

    @Override
    public ExamDetailRes getExamById(Long id) {
        return examService.getExamById(id);
    }

    @Override
    public ExamDeleteRes deleteExam(Long id) {
        ExamDeleteRes res = new ExamDeleteRes();
        res.setId(examService.deleteExam(id));
        return res;
    }

    @Override
    public ExamSaveRes updateAnswerVisible(Long id, Boolean answerVisible) {
        ExamSaveRes res = new ExamSaveRes();
        res.setId(examService.updateAnswerVisible(id, answerVisible));
        return res;
    }

    @Override
    public PageResult<ExamSubmissionPageRes> pageSubmissions(Long examId, ExamSubmissionPageReq req) {
        return examService.pageSubmissions(examId, req.getPageNum(), req.getPageSize());
    }

    @Override
    public ExamSubmissionDetailRes getSubmissionDetail(Long recordId) {
        return examService.getSubmissionDetail(recordId);
    }

    @Override
    public ExamSubmissionGradeRes gradeSubmission(Long recordId, ExamSubmissionGradeReq req) {
        return examService.gradeSubmission(recordId, req);
    }

    private ExamEntity buildExam(ExamSaveReq req) {
        if (!req.getStartTime().before(req.getEndTime())) {
            throw BusinessException.DateError.newInstance("可进入开始时间必须早于可进入截止时间");
        }
        ExamEntity exam = new ExamEntity();
        exam.setId(req.getId());
        exam.setPaperId(req.getPaperId());
        exam.setClassId(req.getClassId());
        exam.setStartTime(req.getStartTime());
        exam.setEndTime(req.getEndTime());
        exam.setDurationMinutes(req.getDurationMinutes());
        exam.setInvigilatorUserId(req.getInvigilatorUserId());
        exam.setAnswerVisible(false);
        return exam;
    }
}
