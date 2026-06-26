package cn.yanque.models.dorm.mapper;

import cn.yanque.models.dorm.pojo.entity.DormBedEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍床位数据访问接口。
 */
public interface DormBedMapper {

    void insert(DormBedEntity bed);

    /** 批量插入床位（房间按容量自动生成时使用）。 */
    void batchInsert(@Param("beds") List<DormBedEntity> beds);

    int updateById(DormBedEntity bed);

    /** 更新床位占用状态与当前入住学生（入住/退宿流转用）。 */
    int updateStatusAndStudent(@Param("id") Long id,
                               @Param("status") String status,
                               @Param("currentStudentId") Long currentStudentId);

    DormBedEntity selectById(@Param("id") Long id);

    List<DormBedEntity> selectPage(@Param("roomId") Long roomId, @Param("status") String status);

    int deleteById(@Param("id") Long id);

    /** 统计某房间下的床位数（房间删除前置校验用）。 */
    int countByRoomId(@Param("roomId") Long roomId);
}
