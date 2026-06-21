package cn.yanque.models.student.coursehour.mapper;

import cn.yanque.models.student.coursehour.pojo.entity.StudentCourseHourLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentCourseHourLogMapper {

    int insert(StudentCourseHourLogEntity entity);
}
