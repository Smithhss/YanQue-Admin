package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增床位请求对象(用于手动补床)。
 */
@Data
@Schema(description = "创建床位请求")
public class DormBedCreateReq {

    /** 所属房间ID */
    @NotNull(message = "房间不能为空")
    @Schema(description = "所属房间ID")
    private Long roomId;

    /** 床位号 */
    @NotBlank(message = "床位号不能为空")
    @Schema(description = "床位号")
    private String bedNo;
}
