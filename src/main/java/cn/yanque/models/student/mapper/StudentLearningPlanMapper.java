package cn.yanque.models.student.mapper;

import cn.yanque.models.student.pojo.bo.QueryStudentLearningPlanBo;
import cn.yanque.models.student.pojo.entity.StudentLearningPlanEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentLearningPlanMapper {

    void insert(StudentLearningPlanEntity plan);

    StudentLearningPlanEntity selectById(@Param("id") Long id);

    StudentLearningPlanEntity selectActiveByStudentId(@Param("studentId") Long studentId);

    List<StudentLearningPlanEntity> selectActiveByStudentIds(@Param("studentIds") List<Long> studentIds);

    List<StudentLearningPlanEntity> selectPage(QueryStudentLearningPlanBo query);
}
