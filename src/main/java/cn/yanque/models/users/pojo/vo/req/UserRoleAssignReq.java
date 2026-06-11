package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 用户分配角色请求对象。
 */
@Data
@Schema(description = "用户分配角色请求")
public class UserRoleAssignReq {

    /** 角色ID列表，提交后会覆盖用户原有角色 */
    @NotNull(message = "角色ID列表不能为空")
    @Schema(description = "角色ID列表，传空数组表示清空用户角色")
    private List<Long> roleIds;
}
