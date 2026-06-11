package cn.yanque.models.users.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色分配权限响应对象。
 */
@Data
@Schema(description = "角色分配权限响应")
public class RolePermissionAssignRes {

    /** 角色ID */
    @Schema(description = "角色ID")
    private Long roleId;

    /** 本次分配后的权限ID列表 */
    @Schema(description = "权限ID列表")
    private List<Long> permissionIds;
}
