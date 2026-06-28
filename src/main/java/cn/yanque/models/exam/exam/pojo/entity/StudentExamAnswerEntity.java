package cn.yanque.models.exam.exam.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生考试答案实体,对应 student_exam_answer 表。
 */
@Data
public class StudentExamAnswerEntity {

    /** 答案ID */
    private Long id;

    /** 学生考试记录ID */
    private Long recordId;

    /** 考试ID */
    private Long examId;

    /** 试卷ID */
    private Long paperId;

    /** 题目ID */
    private Long questionId;

    /** 题型 */
    private String questionType;

    /** 题目分数 */
    private BigDecimal questionScore;

    /** 学生答案内容 */
    private String answerContent;

    /** 是否正确,主观题和编程题为空 */
    private Boolean correct;

    /** 本题得分 */
    private BigDecimal score;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
