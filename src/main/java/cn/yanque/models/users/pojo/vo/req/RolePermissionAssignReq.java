package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色分配权限请求对象。
 */
@Data
@Schema(description = "角色分配权限请求")
public class RolePermissionAssignReq {

    /** 权限ID列表，提交后会覆盖角色原有权限 */
    @NotNull(message = "权限ID列表不能为空")
    @Schema(description = "权限ID列表，传空数组表示清空角色权限")
    private List<Long> permissionIds;
}
