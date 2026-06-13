package cn.yanque.studentFront.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 学生登录请求。
 */
@Data
@Schema(description = "学生登录请求")
public class StudentLoginReq {

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String phone;

    /** 密码，当前阶段前端保留，后端先按手机号和订单支付状态登录 */
    @Schema(description = "密码")
    private String password;
}
