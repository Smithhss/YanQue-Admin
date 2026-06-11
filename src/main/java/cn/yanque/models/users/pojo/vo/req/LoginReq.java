package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求对象。
 */
@Data
@Schema(description = "登录请求")
public class LoginReq {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
