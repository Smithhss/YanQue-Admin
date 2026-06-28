package cn.yanque.models.exam.exam.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生考试记录实体,对应 student_exam_record 表。
 */
@Data
public class StudentExamRecordEntity {

    /** 记录ID */
    private Long id;

    /** 考试ID */
    private Long examId;

    /** 学生ID */
    private Long studentId;

    /** 实际开始答题时间 */
    private Date startTime;

    /** 当前学生本次考试截止时间 */
    private Date deadlineTime;

    /** 提交时间 */
    private Date submitTime;

    /** 状态:IN_PROGRESS / SUBMITTED / TIMEOUT */
    private String status;

    /** 批改状态:PENDING / GRADING / COMPLETED */
    private String gradingStatus;

    /** 得分 */
    private BigDecimal score;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
