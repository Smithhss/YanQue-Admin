package cn.yanque.models.student.coursehour.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生课时流水实体,对应 student_course_hour_log 表。
 */
@Data
public class StudentCourseHourLogEntity {

    /** 课时流水ID */
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 变动类型:CONSUME/ADJUST/REVERT */
    private String changeType;

    /** 变动课时(正为增加,负为减少) */
    private BigDecimal changeHours;

    /** 变动后剩余课时(快照) */
    private BigDecimal remainingAfter;

    /** 关联课次ID(消耗/回滚时记录) */
    private Long scheduleId;

    /** 变动原因(手动调整时填写) */
    private String reason;

    /** 操作人(管理端用户ID) */
    private Long operatorId;

    /** 创建时间 */
    private Date createdAt;
}
