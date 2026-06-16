package cn.yanque.models.exam.exam.mapper;

import cn.yanque.models.exam.exam.pojo.entity.StudentExamAnswerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生考试答案数据访问接口。
 */
public interface StudentExamAnswerMapper {

    void insertBatch(@Param("answers") List<StudentExamAnswerEntity> answers);

    StudentExamAnswerEntity selectById(@Param("id") Long id);

    List<StudentExamAnswerEntity> selectByRecordId(@Param("recordId") Long recordId);

    int updateScore(StudentExamAnswerEntity answer);

    int deleteByRecordId(@Param("recordId") Long recordId);
}
