package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学生考试提交记录响应。
 */
@Data
public class StudentExamSubmissionRes {

    /** 学生考试记录ID */
    private Long recordId;

    /** 考试ID */
    private Long examId;

    /** 试卷ID */
    private Long paperId;

    /** 试卷名称 */
    private String paperName;

    /** 班级期数 */
    private String classPeriod;

    /** 阶段名称,为空表示整门课程考试 */
    private String stageName;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 当前得分,主观题未批改时只包含已评分题目 */
    private BigDecimal score;

    /** 记录状态 */
    private String recordStatus;

    /** 批改状态:PENDING / GRADING / COMPLETED */
    private String gradingStatus;

    /** 是否已向学生公布答卷判定结果 */
    private Boolean answerVisible;

    /** 提交时间 */
    private Date submitTime;

    /** 题目提交记录 */
    private List<StudentExamSubmissionQuestionRes> questions;
}
