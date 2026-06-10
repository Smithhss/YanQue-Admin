package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户分配角色请求")
public class UserRoleAssignReq {

    @NotNull(message = "角色ID列表不能为空")
    @Schema(description = "角色ID列表，传空数组表示清空用户角色")
    private List<Long> roleIds;
}
