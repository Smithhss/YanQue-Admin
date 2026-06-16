package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增课程请求对象。
 */
@Data
@Schema(description = "创建课程请求")
public class CourseCreateReq {

    /** 课程名称 */
    @NotBlank(message = "课程名称不能为空")
    @Schema(description = "课程名称")
    private String courseName;

    /** 课程总天数 */
    @NotNull(message = "课程天数不能为空")
    @Min(value = 1, message = "课程天数不能小于1")
    @Schema(description = "课程天数")
    private Integer courseDays;

    /** 上课方式 */
    @NotBlank(message = "上课方式不能为空")
    @Schema(description = "上课方式：ONLINE线上，OFFLINE线下")
    private String teachingMode;

    /** 课程资料路径 */
    @NotBlank(message = "资料路径不能为空")
    @Schema(description = "资料路径")
    private String materialPath;
}
