package cn.yanque.models.exam.question.pojo.vo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增题目请求。
 */
@Data
public class ExamQuestionCreateReq {

    @NotBlank(message = "题目类型不能为空")
    private String questionType;

    @NotBlank(message = "题干不能为空")
    private String questionContent;

    @NotBlank(message = "正确答案不能为空")
    private String answerContent;

    private String analysisContent;

    @NotBlank(message = "难度不能为空")
    private String difficulty;

    private List<Long> courseIds;

    @Valid
    private List<ExamQuestionCourseStageReq> courseStages;

    @NotBlank(message = "状态不能为空")
    private String status;

    @Valid
    private List<ExamQuestionOptionReq> options;
}
