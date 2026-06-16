package cn.yanque.models.exam.paper.pojo.vo.req;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 试卷题目入参。
 */
@Data
public class ExamPaperQuestionReq {

    @NotNull(message = "题目ID不能为空")
    private Long questionId;

    @NotNull(message = "题目分数不能为空")
    @DecimalMin(value = "0.5", message = "题目分数必须大于0")
    private BigDecimal questionScore;
}
