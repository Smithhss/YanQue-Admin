package cn.yanque.models.users.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新角色响应")
public class RoleUpdateRes {

    @Schema(description = "角色ID")
    private Long id;
}
