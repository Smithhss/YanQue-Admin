package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 床位分页查询请求对象。
 */
@Data
@Schema(description = "床位分页查询请求")
public class DormBedPageReq {

    /** 按房间过滤 */
    @Schema(description = "房间ID过滤")
    private Long roomId;

    /** 按状态过滤:FREE/OCCUPIED/LOCKED */
    @Schema(description = "状态过滤:FREE空闲/OCCUPIED占用/LOCKED锁定")
    private String status;

    /** 当前页码 */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
