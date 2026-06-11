package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改权限请求对象。
 */
@Data
@Schema(description = "更新权限请求")
public class PermissionUpdateReq {

    @Schema(description = "权限ID，由路径参数传入")
    private Long id;

    @Schema(description = "父权限ID，根节点为0")
    private Long parentId = 0L;

    @NotBlank(message = "权限编码不能为空")
    @Schema(description = "权限编码")
    private String permissionCode;

    @NotBlank(message = "权限名称不能为空")
    @Schema(description = "权限名称")
    private String permissionName;

    @NotBlank(message = "权限类型不能为空")
    @Schema(description = "权限类型，例如API、MENU、BUTTON")
    private String permissionType;

    @Schema(description = "API路径匹配规则，仅API权限使用")
    private String apiPath;

    @Schema(description = "排序值，越小越靠前")
    private Integer sortNum;

    @Schema(description = "权限描述")
    private String description;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;
}
