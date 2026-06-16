package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台考试提交记录分页响应。
 */
@Data
public class ExamSubmissionPageRes {

    private Long recordId;

    private Long examId;

    private Long studentId;

    private String studentName;

    private String studentPhone;

    private String recordStatus;

    private String recordStatusText;

    private String gradingStatus;

    private BigDecimal score;

    private Date startTime;

    private Date deadlineTime;

    private Date submitTime;
}
