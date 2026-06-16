package cn.yanque.models.exam.paper.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.paper.pojo.bo.QueryExamPaperBo;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperEntity;
import cn.yanque.models.exam.paper.pojo.entity.ExamPaperQuestionEntity;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDetailRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperPageRes;

import java.util.List;

/**
 * 试卷领域服务。
 */
public interface ExamPaperService {

    Long savePaperWithQuestions(ExamPaperEntity paper, List<ExamPaperQuestionEntity> questions);

    PageResult<ExamPaperPageRes> pagePaper(QueryExamPaperBo query, Integer pageNum, Integer pageSize);

    ExamPaperDetailRes getPaperById(Long id);

    Long deletePaper(Long id);
}
