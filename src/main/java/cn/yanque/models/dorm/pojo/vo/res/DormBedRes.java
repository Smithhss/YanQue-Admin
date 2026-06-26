package cn.yanque.models.dorm.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 床位响应对象（详情与分页列表共用）。
 */
@Data
@Schema(description = "床位响应")
public class DormBedRes {

    /** 床位ID */
    @Schema(description = "床位ID")
    private Long id;

    /** 所属房间ID */
    @Schema(description = "所属房间ID")
    private Long roomId;

    /** 床位号 */
    @Schema(description = "床位号")
    private String bedNo;

    /** 状态 */
    @Schema(description = "状态：FREE空闲/OCCUPIED占用/LOCKED锁定")
    private String status;

    /** 当前入住学生ID */
    @Schema(description = "当前入住学生ID，空闲时为null")
    private Long currentStudentId;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;

    /** 更新时间 */
    @Schema(description = "更新时间")
    private Date updatedAt;
}
