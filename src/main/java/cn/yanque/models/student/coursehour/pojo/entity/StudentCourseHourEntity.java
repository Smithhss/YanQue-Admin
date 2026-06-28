package cn.yanque.models.student.coursehour.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生课时账户实体,对应 student_course_hour 表。
 */
@Data
public class StudentCourseHourEntity {

    /** 课时账户ID */
    private Long id;

    /** 学生ID */
    private Long studentId;

    /** 累计获得课时 */
    private BigDecimal totalHours;

    /** 已消耗课时 */
    private BigDecimal usedHours;

    /** 剩余课时(可为负,表示欠课时) */
    private BigDecimal remainingHours;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
