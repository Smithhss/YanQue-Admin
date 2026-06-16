package cn.yanque.models.exam.paper.biz.impl;

import cn.yanque.common.api.PageResult;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.exam.paper.biz.ExamPaperBiz;
import cn.yanque.models.exam.paper.pojo.bo.QueryExamPaperBo;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperEntity;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperQuestionEntity;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperPageReq;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperQuestionReq;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperSaveReq;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDeleteRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDetailRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperPageRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperSaveRes;
import cn.yanque.models.exam.paper.service.ExamPaperService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamPaperBizImpl implements ExamPaperBiz {

    @Autowired
    private ExamPaperService examPaperService;

    @Override
    public ExamPaperSaveRes savePaperWithQuestions(ExamPaperSaveReq req) {

        // 参数校验
        validateQuestionScore(req);
        ExamPaperEntity paper = new ExamPaperEntity();
        paper.setPaperName(req.getPaperName().trim());
        paper.setCourseId(req.getCourseId());
        paper.setStageName(StringUtils.hasText(req.getStageName()) ? req.getStageName().trim() : null);
        paper.setTotalScore(req.getTotalScore());

        // 构造试卷题目
        List<ExamPaperQuestionEntity> questions = req.getQuestions().stream()
                .map(this::buildQuestionEntity)
                .toList();

        // 保存
        Long id = examPaperService.savePaperWithQuestions(paper, questions);
        ExamPaperSaveRes res = new ExamPaperSaveRes();
        res.setId(id);
        return res;
    }

    @Override
    public PageResult<ExamPaperPageRes> pagePaper(ExamPaperPageReq req) {
        QueryExamPaperBo query = new QueryExamPaperBo();
        BeanUtils.copyProperties(req, query);
        if (StringUtils.hasText(query.getPaperName())) {
            query.setPaperName(query.getPaperName().trim());
        }
        if (StringUtils.hasText(query.getStageName())) {
            query.setStageName(query.getStageName().trim());
        }
        return examPaperService.pagePaper(query, req.getPageNum(), req.getPageSize());
    }

    @Override
    public ExamPaperDetailRes getPaperById(Long id) {
        return examPaperService.getPaperById(id);
    }

    @Override
    public ExamPaperDeleteRes deletePaper(Long id) {
        ExamPaperDeleteRes res = new ExamPaperDeleteRes();
        res.setId(examPaperService.deletePaper(id));
        return res;
    }

    private ExamPaperQuestionEntity buildQuestionEntity(ExamPaperQuestionReq req) {
        ExamPaperQuestionEntity question = new ExamPaperQuestionEntity();
        question.setQuestionId(req.getQuestionId());
        question.setQuestionScore(req.getQuestionScore());
        return question;
    }

    private void validateQuestionScore(ExamPaperSaveReq req) {
        Set<Long> questionIds = new HashSet<>();
        BigDecimal scoreTotal = BigDecimal.ZERO;
        for (ExamPaperQuestionReq question : req.getQuestions()) {
            if (!questionIds.add(question.getQuestionId())) {
                throw BusinessException.DateError.newInstance("试卷题目不能重复");
            }
            scoreTotal = scoreTotal.add(question.getQuestionScore());
        }
        if (scoreTotal.compareTo(req.getTotalScore()) != 0) {
            throw BusinessException.DateError.newInstance("题目分数合计必须等于试卷总分");
        }
    }
}
