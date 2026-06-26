package cn.yanque.models.dorm.mapper;

import cn.yanque.models.dorm.pojo.entity.DormRoomEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍房间数据访问接口。
 */
public interface DormRoomMapper {

    void insert(DormRoomEntity room);

    int updateById(DormRoomEntity room);

    DormRoomEntity selectById(@Param("id") Long id);

    List<DormRoomEntity> selectByIds(@Param("ids") List<Long> ids);

    List<DormRoomEntity> selectPage(@Param("buildingId") Long buildingId, @Param("keyword") String keyword);

    int deleteById(@Param("id") Long id);

    /** 统计某楼栋下的房间数（楼栋删除前置校验用）。 */
    int countByBuildingId(@Param("buildingId") Long buildingId);
}
