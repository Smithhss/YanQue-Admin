package cn.yanque.models.exam.exam.pojo.vo.req;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台考试单题批改请求。
 */
@Data
public class ExamSubmissionGradeAnswerReq {

    @NotNull(message = "答案ID不能为空")
    private Long answerId;

    @NotNull(message = "题目得分不能为空")
    @DecimalMin(value = "0.0", message = "题目得分不能小于0")
    private BigDecimal score;
}
