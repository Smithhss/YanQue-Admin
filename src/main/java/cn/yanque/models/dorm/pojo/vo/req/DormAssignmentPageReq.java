package cn.yanque.models.dorm.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 入住记录分页查询请求对象。
 */
@Data
@Schema(description = "入住记录分页查询请求")
public class DormAssignmentPageReq {

    /** 按楼栋过滤 */
    @Schema(description = "楼栋ID过滤")
    private Long buildingId;

    /** 按状态过滤:LIVING在住/CHECKED_OUT已退宿 */
    @Schema(description = "状态过滤:LIVING在住/CHECKED_OUT已退宿")
    private String status;

    /** 搜索关键字,匹配学生姓名/手机号 */
    @Schema(description = "关键词,匹配学生姓名/手机号")
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
