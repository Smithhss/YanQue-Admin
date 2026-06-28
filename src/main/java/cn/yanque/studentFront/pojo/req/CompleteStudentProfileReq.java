package cn.yanque.studentFront.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学生支付成功后完善资料请求。
 */
@Data
@Schema(description = "学生支付成功后完善资料请求")
public class CompleteStudentProfileReq {

    @NotBlank(message = "支付订单号不能为空")
    @Schema(description = "支付订单号")
    private String orderNo;

    @NotBlank(message = "登录密码不能为空")
    @Schema(description = "登录密码")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码")
    private String confirmPassword;

    @NotBlank(message = "学历不能为空")
    @Schema(description = "学历")
    private String education;

    @NotBlank(message = "性别不能为空")
    @Schema(description = "性别:MALE男/FEMALE女")
    private String gender;

    @NotNull(message = "届数不能为空")
    @Schema(description = "届数")
    private Integer gradeYear;

    @NotBlank(message = "学校不能为空")
    @Schema(description = "学校")
    private String school;

    @Schema(description = "专业")
    private String major;
}
