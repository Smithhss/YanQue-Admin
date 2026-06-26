package cn.yanque.models.dorm.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 宿舍房间响应对象（详情与分页列表共用）。
 */
@Data
@Schema(description = "宿舍房间响应")
public class DormRoomRes {

    /** 房间ID */
    @Schema(description = "房间ID")
    private Long id;

    /** 所属楼栋ID */
    @Schema(description = "所属楼栋ID")
    private Long buildingId;

    /** 房间号 */
    @Schema(description = "房间号")
    private String roomNo;

    /** 楼层 */
    @Schema(description = "楼层")
    private Integer floor;

    /** 床位容量 */
    @Schema(description = "床位容量")
    private Integer capacity;

    /** 房型 */
    @Schema(description = "房型")
    private String roomType;

    /** 状态 */
    @Schema(description = "状态：ENABLED启用/DISABLED停用/MAINTENANCE维修")
    private String status;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
