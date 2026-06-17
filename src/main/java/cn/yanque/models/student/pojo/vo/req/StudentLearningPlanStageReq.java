package cn.yanque.models.student.pojo.vo.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentLearningPlanStageReq {

    @NotBlank(message = "阶段不能为空")
    private String stageName;

    @NotNull(message = "阶段学习天数不能为空")
    @Min(value = 1, message = "阶段学习天数不能小于1")
    private Integer stageDays;
}
