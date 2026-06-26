package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改宿舍房间请求对象。
 */
@Data
@Schema(description = "修改宿舍房间请求")
public class DormRoomUpdateReq {

    /** 房间ID，由Controller从路径参数写入 */
    @Schema(description = "房间ID", hidden = true)
    private Long id;

    /** 所属楼栋ID */
    @NotNull(message = "楼栋不能为空")
    @Schema(description = "所属楼栋ID")
    private Long buildingId;

    /** 房间号 */
    @NotBlank(message = "房间号不能为空")
    @Schema(description = "房间号")
    private String roomNo;

    /** 楼层 */
    @Schema(description = "楼层")
    private Integer floor;

    /** 床位容量 */
    @NotNull(message = "床位容量不能为空")
    @Min(value = 1, message = "床位容量不能小于1")
    @Schema(description = "床位容量")
    private Integer capacity;

    /** 房型 */
    @Schema(description = "房型")
    private String roomType;

    /** 状态：ENABLED/DISABLED/MAINTENANCE */
    @Schema(description = "状态：ENABLED启用/DISABLED停用/MAINTENANCE维修")
    private String status;
}
