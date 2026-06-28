package cn.yanque.models.dorm.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 宿舍房间实体,对应 dorm_room 表。
 */
@Data
public class DormRoomEntity {

    /** 房间ID */
    private Long id;

    /** 所属楼栋ID(dorm_building.id) */
    private Long buildingId;

    /** 房间号 */
    private String roomNo;

    /** 楼层 */
    private Integer floor;

    /** 床位容量 */
    private Integer capacity;

    /** 房型:FOUR四人间/SIX六人间等 */
    private String roomType;

    /** 状态:ENABLED启用/DISABLED停用/MAINTENANCE维修 */
    private String status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
