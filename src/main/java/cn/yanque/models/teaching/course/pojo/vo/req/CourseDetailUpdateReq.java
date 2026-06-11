package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改课程详情请求")
public class CourseDetailUpdateReq {

    @Schema(description = "课程详情ID")
    private Long id;

    @NotBlank(message = "阶段不能为空")
    @Schema(description = "阶段")
    private String stageName;

    @NotNull(message = "第几天不能为空")
    @Min(value = 1, message = "第几天不能小于1")
    @Schema(description = "第几天")
    private Integer dayNumber;

    @NotBlank(message = "上课内容不能为空")
    @Schema(description = "上课内容")
    private String classContent;
}
