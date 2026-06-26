package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 新增宿舍房间请求对象。
 */
@Data
@Schema(description = "创建宿舍房间请求")
public class DormRoomCreateReq {

    /** 所属楼栋ID */
    @NotNull(message = "楼栋不能为空")
    @Schema(description = "所属楼栋ID")
    private Long buildingId;

    /** 房间号 */
    @NotBlank(message = "房间号不能为空")
    @Schema(description = "房间号")
    private String roomNo;

    /** 楼层 */
    @Schema(description = "楼层", defaultValue = "1")
    private Integer floor = 1;

    /** 床位容量 */
    @NotNull(message = "床位容量不能为空")
    @Min(value = 1, message = "床位容量不能小于1")
    @Schema(description = "床位容量")
    private Integer capacity;

    /** 房型 */
    @Schema(description = "房型：FOUR四人间/SIX六人间等")
    private String roomType;

    /** 是否按容量自动生成床位（默认是） */
    @Schema(description = "是否按容量自动生成床位", defaultValue = "true")
    private Boolean autoGenerateBeds = true;
}
