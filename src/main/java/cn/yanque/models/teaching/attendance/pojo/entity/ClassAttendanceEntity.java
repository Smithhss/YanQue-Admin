package cn.yanque.models.teaching.attendance.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 班级考勤记录实体,对应 class_attendance 表。
 */
@Data
public class ClassAttendanceEntity {

    /** 考勤记录ID */
    private Long id;

    /** 课次ID */
    private Long scheduleId;

    /** 班级ID */
    private Long classId;

    /** 学生ID */
    private Long studentId;

    /** 上课日期 */
    private Date scheduleDate;

    /** 考勤状态:PRESENT/LATE/LEAVE/ABSENT */
    private String status;

    /** 请假/备注原因 */
    private String leaveReason;

    /** 本次扣减课时(快照) */
    private BigDecimal hourDeducted;

    /** 点名操作人 */
    private Long operatorId;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
