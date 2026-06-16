package cn.yanque.models.exam.question.mapper;

import cn.yanque.models.exam.question.pojo.bo.QueryExamQuestionBo;
import cn.yanque.models.exam.question.pojo.entity.ExamQuestionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题库题目数据访问接口。
 */
public interface ExamQuestionMapper {

    void insert(ExamQuestionEntity question);

    int updateById(ExamQuestionEntity question);

    int updateStatusById(@Param("id") Long id, @Param("status") String status);

    ExamQuestionEntity selectById(@Param("id") Long id);

    List<ExamQuestionEntity> selectPage(QueryExamQuestionBo query);

    int deleteById(@Param("id") Long id);
}
