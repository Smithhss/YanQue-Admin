package cn.yanque.models.exam.question.pojo.vo.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 题目关联课程阶段请求。
 */
@Data
public class ExamQuestionCourseStageReq {

    @NotNull(message = "课程不能为空")
    private Long courseId;

    @NotBlank(message = "阶段不能为空")
    private String stageName;
}
