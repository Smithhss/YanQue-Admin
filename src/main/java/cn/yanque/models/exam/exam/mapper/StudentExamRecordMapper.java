package cn.yanque.models.exam.exam.mapper;

import cn.yanque.models.exam.exam.pojo.entity.StudentExamRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生考试记录数据访问接口。
 */
public interface StudentExamRecordMapper {

    void insert(StudentExamRecordEntity record);

    StudentExamRecordEntity selectById(@Param("id") Long id);

    StudentExamRecordEntity selectByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);

    List<StudentExamRecordEntity> selectByExamId(@Param("examId") Long examId);

    List<StudentExamRecordEntity> selectByExamIdsAndStudentId(@Param("examIds") List<Long> examIds,
                                                              @Param("studentId") Long studentId);

    int updateSubmit(StudentExamRecordEntity record);

    int updateGrade(StudentExamRecordEntity record);
}
