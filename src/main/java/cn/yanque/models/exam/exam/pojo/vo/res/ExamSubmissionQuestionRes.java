package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 后台考试答卷题目响应。
 */
@Data
public class ExamSubmissionQuestionRes {

    private Long answerId;

    private Long questionId;

    private String questionContent;

    private String questionType;

    private BigDecimal questionScore;

    private String answerContent;

    private String correctAnswer;

    private String analysisContent;

    private Boolean correct;

    private BigDecimal score;

    private List<ExamSubmissionOptionRes> options;
}
