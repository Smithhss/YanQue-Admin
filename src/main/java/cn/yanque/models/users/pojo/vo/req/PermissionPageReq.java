package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "权限分页查询请求")
public class PermissionPageReq {

    @Schema(description = "关键词，匹配权限编码、权限名称、API路径")
    private String keyword;

    @Schema(description = "父权限ID")
    private Long parentId;

    @Schema(description = "权限类型，例如API、MENU、BUTTON")
    private String permissionType;

    @Schema(description = "状态", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;

    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Schema(description = "每页条数", defaultValue = "10")
    private Integer pageSize = 10;
}
