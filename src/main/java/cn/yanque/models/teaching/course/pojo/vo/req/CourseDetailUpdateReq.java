package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改课程详情请求对象。
 */
@Data
@Schema(description = "修改课程详情请求")
public class CourseDetailUpdateReq {

    /** 课程详情ID */
    @Schema(description = "课程详情ID")
    private Long id;

    /** 阶段名称 */
    @NotBlank(message = "阶段不能为空")
    @Schema(description = "阶段")
    private String stageName;

    /** 第几天 */
    @Min(value = 1, message = "第几天不能小于1")
    @Schema(description = "第几天，线下课程必填")
    private Integer dayNumber;

    /** 上课内容 */
    @Schema(description = "上课内容，线下课程必填")
    private String classContent;
}
