package cn.yanque.models.student.coursehour.mapper;

import cn.yanque.models.student.coursehour.pojo.entity.StudentCourseHourEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface StudentCourseHourMapper {

    StudentCourseHourEntity selectByStudentId(@Param("studentId") Long studentId);

    int insert(StudentCourseHourEntity entity);

    /**
     * 原子增量更新账户:各字段在数据库侧自增,避免读改写竞态。
     */
    int addHours(@Param("studentId") Long studentId,
                 @Param("deltaTotal") BigDecimal deltaTotal,
                 @Param("deltaUsed") BigDecimal deltaUsed,
                 @Param("deltaRemaining") BigDecimal deltaRemaining);

    List<StudentCourseHourEntity> selectPage(@Param("studentId") Long studentId);
}
