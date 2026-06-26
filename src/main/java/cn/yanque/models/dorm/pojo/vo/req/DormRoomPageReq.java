package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 宿舍房间分页查询请求对象。
 */
@Data
@Schema(description = "宿舍房间分页查询请求")
public class DormRoomPageReq {

    /** 按楼栋过滤 */
    @Schema(description = "楼栋ID过滤")
    private Long buildingId;

    /** 搜索关键字，匹配房间号 */
    @Schema(description = "关键词，匹配房间号")
    private String keyword;

    /** 当前页码 */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    /** 每页条数 */
    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
