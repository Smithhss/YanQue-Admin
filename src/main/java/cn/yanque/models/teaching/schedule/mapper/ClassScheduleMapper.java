package cn.yanque.models.teaching.schedule.mapper;

import cn.yanque.models.teaching.schedule.pojo.entity.ClassScheduleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 班级课表数据访问接口。
 */
public interface ClassScheduleMapper {

    /**
     * 批量插入班级课表。
     *
     * @param list 课表列表
     */
    void batchInsert(@Param("list") List<ClassScheduleEntity> list);

    /**
     * 查询班级课表。
     *
     * @param classId 班级ID
     * @return 课表列表
     */
    List<ClassScheduleEntity> selectByClassId(@Param("classId") Long classId);

    /**
     * 查询班级某一天的课表。
     *
     * @param classId   班级ID
     * @param startDate 日期开始时间
     * @param endDate   日期结束时间
     * @return 课表记录
     */
    ClassScheduleEntity selectByClassIdAndDate(@Param("classId") Long classId,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);

    /**
     * 删除班级下的全部课表。
     *
     * @param classId 班级ID
     * @return 影响行数
     */
    int deleteByClassId(@Param("classId") Long classId);


    List<ClassScheduleEntity> selectByCourseIds(@Param("ids") List<Long> courseIds, @Param("classId") Long classId);

    /**
     * 查询某个日期范围内已经被其他班级占用的老师ID。
     *
     * @param stageStartDate 阶段开始日期
     * @param stageEndDate   阶段结束日期
     * @param classId        当前班级ID
     * @return 老师ID列表
     */
    List<Long> selectTeacheringUserId(@Param("stageStartDate") Date stageStartDate,
                                       @Param("stageEndDate") Date stageEndDate,
                                       @Param("classId") Long classId);

    /**
     * 按课程详情ID批量更新课表老师。
     *
     * @param classId         班级ID
     * @param courseDetailIds 课程详情ID列表
     * @param teacherId       老师ID
     * @return 影响行数
     */
    int updateTeacherByCourseDetailIds(@Param("classId") Long classId,
                                        @Param("courseDetailIds") List<Long> courseDetailIds,
                                        @Param("teacherId") Long teacherId);

    /**
     * 查询当前日期后面所有的课表
     */
    List<ClassScheduleEntity> selectByClassIdAndAfterScheduleDate(@Param("classId") Long classId, @Param("date")Date date);

    /**
     * 删除当前日期后面所有的课表。
     *
     * @param classId 班级ID
     * @param date    开始日期
     * @return 影响行数
     */
    int deleteByClassIdAndAfterScheduleDate(@Param("classId") Long classId, @Param("date") Date date);

    /**
     * 查询老师同一天重复上课的分组数量。
     *
     * @return 重复分组数量
     */
    int countDuplicateTeacherSchedule();
}
