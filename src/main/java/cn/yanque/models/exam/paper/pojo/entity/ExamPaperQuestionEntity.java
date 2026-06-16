package cn.yanque.models.exam.paper.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 试卷题目关联实体，对应 exam_paper_question 表。
 */
@Data
public class ExamPaperQuestionEntity {

    /** 关联ID */
    private Long id;

    /** 试卷ID */
    private Long paperId;

    /** 题目ID */
    private Long questionId;

    /** 题目分数 */
    private BigDecimal questionScore;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
