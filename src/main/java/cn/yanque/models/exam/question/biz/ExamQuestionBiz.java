package cn.yanque.models.exam.question.biz;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionCreateReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionPageReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionStatusReq;
import cn.yanque.models.exam.question.pojo.vo.req.ExamQuestionUpdateReq;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionCreateRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDeleteRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDetailRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionPageRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionStatusRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionUpdateRes;

/**
 * 题库业务编排。
 */
public interface ExamQuestionBiz {

    ExamQuestionCreateRes addQuestion(ExamQuestionCreateReq req);

    ExamQuestionUpdateRes updateQuestion(ExamQuestionUpdateReq req);

    ExamQuestionDeleteRes deleteQuestion(Long id);

    ExamQuestionDetailRes getQuestionById(Long id);

    PageResult<ExamQuestionPageRes> pageQuestion(ExamQuestionPageReq req);

    ExamQuestionStatusRes updateStatus(Long id, ExamQuestionStatusReq req);
}
