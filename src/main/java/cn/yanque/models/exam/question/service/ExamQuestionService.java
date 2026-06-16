package cn.yanque.models.exam.question.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.exam.question.pojo.bo.QueryExamQuestionBo;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionCourseEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionOptionEntity;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionDetailRes;
import cn.yanque.models.exam.question.pojo.vo.res.ExamQuestionPageRes;

import java.util.List;

/**
 * 题库领域能力。
 */
public interface ExamQuestionService {

    Long createQuestion(ExamQuestionEntity question, List<ExamQuestionCourseEntity> courseStages, List<ExamQuestionOptionEntity> options);

    Long updateQuestion(ExamQuestionEntity question, List<ExamQuestionCourseEntity> courseStages, List<ExamQuestionOptionEntity> options);

    Long deleteQuestion(Long id);

    ExamQuestionDetailRes getQuestionById(Long id);

    PageResult<ExamQuestionPageRes> pageQuestion(QueryExamQuestionBo query, Integer pageNum, Integer pageSize);

    Long updateStatus(Long id, String status);
}
