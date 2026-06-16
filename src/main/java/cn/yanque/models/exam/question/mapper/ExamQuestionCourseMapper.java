package cn.yanque.models.exam.question.mapper;

import cn.yanque.models.exam.question.pojo.entity.ExamQuestionCourseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目关联课程数据访问接口。
 */
public interface ExamQuestionCourseMapper {

    void insertBatch(@Param("relations") List<ExamQuestionCourseEntity> relations);

    List<ExamQuestionCourseEntity> selectByQuestionId(@Param("questionId") Long questionId);

    List<ExamQuestionCourseEntity> selectByQuestionIds(@Param("questionIds") List<Long> questionIds);

    int deleteByQuestionId(@Param("questionId") Long questionId);
}
