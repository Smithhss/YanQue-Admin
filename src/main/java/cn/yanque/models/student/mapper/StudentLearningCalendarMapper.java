package cn.yanque.models.student.mapper;

import cn.yanque.models.student.pojo.entity.StudentLearningCalendarEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentLearningCalendarMapper {

    void batchInsert(@Param("list") List<StudentLearningCalendarEntity> list);

    List<StudentLearningCalendarEntity> selectByPlanId(@Param("planId") Long planId);
}
