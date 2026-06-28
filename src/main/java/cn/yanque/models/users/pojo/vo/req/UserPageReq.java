package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 用户分页查询请求对象。
 */
@Data
@Schema(description = "用户分页查询请求")
public class UserPageReq {

    @Schema(description = "关键词,匹配用户名,昵称,真实姓名")
    private String keyword;

    @Schema(description = "状态", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;

    @Schema(description = "角色编码")
    private String roleCode;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
