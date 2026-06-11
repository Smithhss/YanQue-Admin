package cn.yanque.models.teaching.clazz.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改班级请求")
public class ClazzUpdateReq {

    @Schema(description = "班级ID")
    private Long id;

    @NotBlank(message = "班级期数不能为空")
    @Schema(description = "班级期数")
    private String classPeriod;

    @NotNull(message = "班主任ID不能为空")
    @Schema(description = "班主任ID")
    private Long headTeacherId;

    @NotNull(message = "校区ID不能为空")
    @Schema(description = "校区ID")
    private Long campusId;

    @NotNull(message = "课程ID不能为空")
    @Schema(description = "课程ID")
    private Long courseId;
}
