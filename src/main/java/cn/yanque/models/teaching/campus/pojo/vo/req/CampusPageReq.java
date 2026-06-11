package cn.yanque.models.teaching.campus.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 校区分页查询请求对象。
 */
@Data
@Schema(description = "校区分页查询请求")
public class CampusPageReq {

    /** 搜索关键字，匹配校区地点、负责人、负责人电话 */
    @Schema(description = "关键词，匹配校区地点、负责人、负责人电话")
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
