package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生交卷响应。
 */
@Data
public class StudentExamSubmitRes {

    /** 学生考试记录ID */
    private Long recordId;

    /** 考试ID */
    private Long examId;

    /** 状态 */
    private String status;

    /** 批改状态:PENDING / GRADING / COMPLETED */
    private String gradingStatus;

    /** 提交时间 */
    private Date submitTime;

    /** 当前已计算得分,主观题未批改时只包含客观题得分 */
    private BigDecimal score;
}
