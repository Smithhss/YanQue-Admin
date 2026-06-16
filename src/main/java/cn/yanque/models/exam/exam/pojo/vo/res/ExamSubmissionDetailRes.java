package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 后台考试答卷详情响应。
 */
@Data
public class ExamSubmissionDetailRes {

    private Long recordId;

    private Long examId;

    private Long paperId;

    private String paperName;

    private String classPeriod;

    private String stageName;

    private Long studentId;

    private String studentName;

    private String studentPhone;

    private BigDecimal totalScore;

    private BigDecimal score;

    private String recordStatus;

    private String gradingStatus;

    private Date startTime;

    private Date deadlineTime;

    private Date submitTime;

    private List<ExamSubmissionQuestionRes> questions;
}
