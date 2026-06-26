package cn.yanque.models.dorm.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 宿舍床位实体，对应 dorm_bed 表。
 */
@Data
public class DormBedEntity {

    /** 床位ID */
    private Long id;

    /** 所属房间ID（dorm_room.id） */
    private Long roomId;

    /** 床位号 */
    private String bedNo;

    /** 状态：FREE空闲/OCCUPIED占用/LOCKED锁定 */
    private String status;

    /** 当前入住学生ID（student.id），空闲时为null */
    private Long currentStudentId;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
