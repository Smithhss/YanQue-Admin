package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增课程详情请求对象。
 */
@Data
@Schema(description = "创建课程详情请求")
public class CourseDetailCreateReq {

    /** 阶段名称 */
    @NotBlank(message = "阶段不能为空")
    @Schema(description = "阶段")
    private String stageName;

    /** 第几天 */
    @NotNull(message = "第几天不能为空")
    @Min(value = 1, message = "第几天不能小于1")
    @Schema(description = "第几天")
    private Integer dayNumber;

    /** 上课内容 */
    @NotBlank(message = "上课内容不能为空")
    @Schema(description = "上课内容")
    private String classContent;
}
