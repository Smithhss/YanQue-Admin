package cn.yanque.models.exam.paper.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 试卷题目响应。
 */
@Data
public class ExamPaperQuestionRes {

    private Long id;

    private Long paperId;

    private Long questionId;

    private String questionContent;

    private String questionType;

    private String difficulty;

    private BigDecimal questionScore;
}
