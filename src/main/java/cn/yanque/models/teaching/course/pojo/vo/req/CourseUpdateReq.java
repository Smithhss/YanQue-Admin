package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改课程请求")
public class CourseUpdateReq {

    @Schema(description = "课程ID")
    private Long id;

    @NotBlank(message = "课程名称不能为空")
    @Schema(description = "课程名称")
    private String courseName;

    @NotNull(message = "课程天数不能为空")
    @Min(value = 1, message = "课程天数不能小于1")
    @Schema(description = "课程天数")
    private Integer courseDays;

    @NotBlank(message = "资料路径不能为空")
    @Schema(description = "资料路径")
    private String materialPath;
}
