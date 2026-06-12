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
     * 删除班级下的全部课表。
     *
     * @param classId 班级ID
     * @return 影响行数
     */
    int deleteByClassId(@Param("classId") Long classId);


    List<ClassScheduleEntity> selectByCourseIds(@Param("ids") List<Long> courseIds, @Param("classId") Long classId);

    List<Long> selectTeacheringUserId(@Param("stageStartDate") Date stageStartDate, @Param("stageEndDate") Date stageEndDate);
}
