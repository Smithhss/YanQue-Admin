package cn.yanque.models.exam.question.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 题库题目实体,对应 exam_question 表。
 */
@Data
public class ExamQuestionEntity {

    /** 题目ID */
    private Long id;

    /** 题目类型:SINGLE单选,MULTIPLE多选,JUDGE判断,FILL填空,SHORT简答,PROGRAMMING编程 */
    private String questionType;

    /** 题干 */
    private String questionContent;

    /** 正确答案 */
    private String answerContent;

    /** 答案解析 */
    private String analysisContent;

    /** 难度:VERY_EASY很简单,EASY简单,NORMAL普通,HARD困难,VERY_HARD很困难 */
    private String difficulty;

    /** 状态:ENABLED启用,DISABLED停用 */
    private String status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
