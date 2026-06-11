package cn.yanque.models.users.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户分配角色响应对象。
 */
@Data
@Schema(description = "用户分配角色响应")
public class UserRoleAssignRes {

    /** 用户ID */
    @Schema(description = "用户ID")
    private Long userId;

    /** 本次分配后的角色ID列表 */
    @Schema(description = "角色ID列表")
    private List<Long> roleIds;
}
