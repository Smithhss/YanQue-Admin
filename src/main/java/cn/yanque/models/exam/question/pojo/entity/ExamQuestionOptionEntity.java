package cn.yanque.models.exam.question.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 题目选项实体,对应 exam_question_option 表。
 */
@Data
public class ExamQuestionOptionEntity {

    /** 选项ID */
    private Long id;

    /** 题目ID */
    private Long questionId;

    /** 选项标识:A/B/C/D */
    private String optionKey;

    /** 选项内容 */
    private String optionContent;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
