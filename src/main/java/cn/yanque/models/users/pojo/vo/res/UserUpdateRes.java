package cn.yanque.models.users.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新用户响应")
public class UserUpdateRes {

    @Schema(description = "用户ID")
    private Long id;
}
