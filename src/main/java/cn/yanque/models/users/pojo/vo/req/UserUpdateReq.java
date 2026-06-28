package cn.yanque.models.users.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改用户请求对象。
 */
@Data
@Schema(description = "更新用户请求")
public class UserUpdateReq {

    @Schema(description = "用户ID,由路径参数传入")
    private Long id;

    @NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "电话")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "飞书 union_id")
    private String unionId;

    @NotBlank(message = "状态不能为空")
    @Schema(description = "状态", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;
}
