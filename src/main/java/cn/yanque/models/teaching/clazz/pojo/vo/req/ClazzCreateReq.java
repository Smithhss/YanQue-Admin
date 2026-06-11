package cn.yanque.models.teaching.clazz.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增班级请求对象。
 */
@Data
@Schema(description = "创建班级请求")
public class ClazzCreateReq {

    /** 班级期数 */
    @NotBlank(message = "班级期数不能为空")
    @Schema(description = "班级期数")
    private String classPeriod;

    /** 班主任用户ID */
    @NotNull(message = "班主任ID不能为空")
    @Schema(description = "班主任ID")
    private Long headTeacherId;

    /** 校区ID */
    @NotNull(message = "校区ID不能为空")
    @Schema(description = "校区ID")
    private Long campusId;

    /** 课程ID */
    @NotNull(message = "课程ID不能为空")
    @Schema(description = "课程ID")
    private Long courseId;
}
