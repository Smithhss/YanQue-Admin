package cn.yanque.models.exam.question.mapper;

import cn.yanque.models.exam.question.pojo.entity.ExamQuestionOptionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目选项数据访问接口。
 */
public interface ExamQuestionOptionMapper {

    void insertBatch(@Param("options") List<ExamQuestionOptionEntity> options);

    List<ExamQuestionOptionEntity> selectByQuestionId(@Param("questionId") Long questionId);

    int deleteByQuestionId(@Param("questionId") Long questionId);
}
