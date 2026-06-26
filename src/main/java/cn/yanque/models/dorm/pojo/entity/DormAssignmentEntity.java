package cn.yanque.models.dorm.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 宿舍入住记录实体，对应 dorm_assignment 表。
 */
@Data
public class DormAssignmentEntity {

    /** 入住记录ID */
    private Long id;

    /** 学生ID（student.id） */
    private Long studentId;

    /** 床位ID（dorm_bed.id） */
    private Long bedId;

    /** 房间ID（冗余，便于查询） */
    private Long roomId;

    /** 楼栋ID（冗余，便于查询） */
    private Long buildingId;

    /** 入住日期 */
    private Date checkInDate;

    /** 退宿日期，在住时为null */
    private Date checkOutDate;

    /** 状态：LIVING在住/CHECKED_OUT已退宿 */
    private String status;

    /** 分配操作人（管理端用户ID） */
    private Long assignedBy;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
