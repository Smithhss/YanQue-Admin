package cn.yanque.models.exam.paper.pojo.vo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保存试卷和题目入参。
 */
@Data
public class ExamPaperSaveReq {

    @NotBlank(message = "试卷名称不能为空")
    private String paperName;

    @NotNull(message = "课程不能为空")
    private Long courseId;

    /** 阶段名称为空表示整门课程考试。 */
    private String stageName;

    @NotNull(message = "总分数不能为空")
    @DecimalMin(value = "1", message = "总分数必须大于0")
    private BigDecimal totalScore;

    @Valid
    @NotEmpty(message = "试卷题目不能为空")
    private List<ExamPaperQuestionReq> questions;
}
