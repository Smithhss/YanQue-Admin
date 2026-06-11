package cn.yanque.models.teaching.course.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 课程分页查询请求对象。
 */
@Data
@Schema(description = "课程分页查询请求")
public class CoursePageReq {

    /** 搜索关键字，匹配课程名称或资料路径 */
    @Schema(description = "关键词，匹配课程名称或资料路径")
    private String keyword;

    /** 当前页码 */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
