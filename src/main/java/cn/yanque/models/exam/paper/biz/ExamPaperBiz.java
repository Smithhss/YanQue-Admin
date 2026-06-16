package cn.yanque.models.exam.paper.biz;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperPageReq;
import cn.yanque.models.exam.paper.pojo.vo.req.ExamPaperSaveReq;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDeleteRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperDetailRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperPageRes;
import cn.yanque.models.exam.paper.pojo.vo.res.ExamPaperSaveRes;

/**
 * 试卷业务编排。
 */
public interface ExamPaperBiz {

    ExamPaperSaveRes savePaperWithQuestions(ExamPaperSaveReq req);

    PageResult<ExamPaperPageRes> pagePaper(ExamPaperPageReq req);

    ExamPaperDetailRes getPaperById(Long id);

    ExamPaperDeleteRes deletePaper(Long id);
}
