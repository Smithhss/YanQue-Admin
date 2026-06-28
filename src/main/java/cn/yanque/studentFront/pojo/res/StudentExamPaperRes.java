package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学生考试试卷响应。
 */
@Data
public class StudentExamPaperRes {

    /** 学生考试记录ID */
    private Long recordId;

    /** 考试ID */
    private Long examId;

    /** 试卷ID */
    private Long paperId;

    /** 试卷名称 */
    private String paperName;

    /** 班级ID */
    private Long classId;

    /** 班级期数 */
    private String classPeriod;

    /** 课程ID */
    private Long courseId;

    /** 阶段名称,为空表示整门课程考试 */
    private String stageName;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 学生实际开始答题时间 */
    private Date startTime;

    /** 当前学生本次考试截止时间 */
    private Date deadlineTime;

    /** 记录状态 */
    private String recordStatus;

    /** 批改状态:PENDING / GRADING / COMPLETED */
    private String gradingStatus;

    /** 题目列表 */
    private List<StudentExamPaperQuestionRes> questions;
}
