package cn.yanque.models.teaching.clazz.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 班级分页查询请求对象。
 */
@Data
@Schema(description = "班级分页查询请求")
public class ClazzPageReq {

    /** 搜索关键字，匹配班级期数 */
    @Schema(description = "关键词，匹配班级期数")
    private String keyword;

    /** 班主任用户ID */
    @Schema(description = "班主任ID")
    private Long headTeacherId;

    /** 校区ID */
    @Schema(description = "校区ID")
    private Long campusId;

    /** 课程ID */
    @Schema(description = "课程ID")
    private Long courseId;

    /** 当前页码 */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
