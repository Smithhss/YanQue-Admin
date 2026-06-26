package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改床位请求对象（改床位号或锁定/解锁）。
 */
@Data
@Schema(description = "修改床位请求")
public class DormBedUpdateReq {

    /** 床位ID，由Controller从路径参数写入 */
    @Schema(description = "床位ID", hidden = true)
    private Long id;

    /** 床位号 */
    @NotBlank(message = "床位号不能为空")
    @Schema(description = "床位号")
    private String bedNo;

    /** 状态：FREE空闲/LOCKED锁定（OCCUPIED由入住流程维护，不在此手改） */
    @Schema(description = "状态：FREE空闲/LOCKED锁定")
    private String status;
}
