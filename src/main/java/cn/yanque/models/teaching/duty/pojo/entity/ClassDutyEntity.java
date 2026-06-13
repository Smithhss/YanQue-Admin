package cn.yanque.models.teaching.duty.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 值班实体，对应 sys_class_duty 表。
 */
@Data
public class ClassDutyEntity {

    /** 值班ID */
    private Long id;

    /** 班级ID，校区统一值班时可为空 */
    private Long classId;

    /** 校区ID */
    private Long campusId;

    /** 老师ID */
    private Long teacherId;

    /** 值班日期 */
    private Date dutyDate;

    /** 值班类型 */
    private String dutyType;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
