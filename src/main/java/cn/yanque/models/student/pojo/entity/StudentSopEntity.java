package cn.yanque.models.student.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生入学SOP记录。
 */
@Data
public class StudentSopEntity {

    /** 主键ID */
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 导师用户ID */
    private Long mentorId;

    /** SOP视频对象Key */
    private String sopVideoObjectKey;

    /** SOP视频文件名 */
    private String sopVideoFileName;

    /** SOP时间 */
    private Date sopTime;

    /** 状态:ASSIGNED已分配,CANCELED已取消 */
    private String status;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
