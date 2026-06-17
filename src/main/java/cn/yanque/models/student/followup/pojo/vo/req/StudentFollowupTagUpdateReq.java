package cn.yanque.models.student.followup.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改学生回访标签配置请求")
public class StudentFollowupTagUpdateReq {

    @Schema(description = "配置ID")
    private Long id;

    @NotBlank(message = "学生标签不能为空")
    @Schema(description = "学生标签")
    private String studentTag;

    @NotNull(message = "回访间隔天数不能为空")
    @Min(value = 1, message = "回访间隔天数不能小于1")
    @Schema(description = "回访间隔天数")
    private Integer followupIntervalDays;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
