package cn.yanque.models.teaching.duty.mapper;

import cn.yanque.models.teaching.duty.pojo.entity.ClassDutyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 值班表数据访问接口。
 */
public interface ClassDutyMapper {

    void insert(ClassDutyEntity duty);

    int updateById(ClassDutyEntity duty);

    ClassDutyEntity selectById(@Param("id") Long id);

    List<ClassDutyEntity> selectByDutyDate(@Param("dutyDate") Date dutyDate);

    List<ClassDutyEntity> selectPage(@Param("classId") Long classId,
                                     @Param("campusId") Long campusId,
                                     @Param("teacherId") Long teacherId,
                                     @Param("dutyType") String dutyType,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);

    int deleteById(@Param("id") Long id);

    int deleteByDutyDate(@Param("dutyDate") Date dutyDate);

    int countClassDuty(@Param("id") Long id,
                       @Param("classId") Long classId,
                       @Param("dutyDate") Date dutyDate,
                       @Param("dutyType") String dutyType);

    int countCampusDuty(@Param("id") Long id,
                        @Param("campusId") Long campusId,
                        @Param("dutyDate") Date dutyDate,
                        @Param("dutyType") String dutyType);

    int countTeacherTimeConflict(@Param("id") Long id,
                                 @Param("teacherId") Long teacherId,
                                 @Param("dutyDate") Date dutyDate,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);
}
