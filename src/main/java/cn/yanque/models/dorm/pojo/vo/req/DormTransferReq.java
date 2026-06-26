package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 调宿请求对象：把学生当前在住床位调换到新床位。
 */
@Data
@Schema(description = "调宿请求")
public class DormTransferReq {

    /** 学生ID */
    @NotNull(message = "学生不能为空")
    @Schema(description = "学生ID")
    private Long studentId;

    /** 目标新床位ID */
    @NotNull(message = "目标床位不能为空")
    @Schema(description = "目标新床位ID")
    private Long newBedId;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
