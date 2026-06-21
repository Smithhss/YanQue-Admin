package cn.yanque.models.teaching.attendance.mapper;

import cn.yanque.models.teaching.attendance.pojo.entity.ClassAttendanceEntity;
import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttendanceMapper {

    /** 只读查询课次（schedule 域无 selectById，此处自建以避免改动其文件）。 */
    ClassScheduleEntity selectScheduleById(@Param("scheduleId") Long scheduleId);

    List<ClassAttendanceEntity> selectByScheduleId(@Param("scheduleId") Long scheduleId);

    int insert(ClassAttendanceEntity entity);

    int updateById(ClassAttendanceEntity entity);

    List<ClassAttendanceEntity> selectPage(@Param("classId") Long classId,
                                           @Param("studentId") Long studentId,
                                           @Param("dateFrom") String dateFrom,
                                           @Param("dateTo") String dateTo);
}
