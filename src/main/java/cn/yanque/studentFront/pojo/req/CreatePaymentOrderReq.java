package cn.yanque.studentFront.pojo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "学生支付下单请求")
public class CreatePaymentOrderReq {

    /** 支付订单号 */
    @NotBlank(message = "支付订单号不能为空")
    @Schema(description = "支付订单号，由生成订单号接口返回", example = "1010202606131416420101123456")
    private String orderNo;

    /** 学生手机号 */
    @NotBlank(message = "学生手机号不能为空")
    @Schema(description = "学生手机号", example = "13293920194")
    private String studentPhone;

    /** 学生姓名 */
    @NotBlank(message = "学生姓名不能为空")
    @Schema(description = "学生姓名", example = "张三")
    private String studentName;

    /** 产品ID */
    @NotBlank(message = "产品ID不能为空")
    @Schema(description = "产品ID", example = "1")
    private String productId;

    /** 支付订单金额 */
    @NotNull(message = "支付订单金额不能为空")
    @DecimalMin(value = "0.01", message = "支付订单金额必须大于0")
    @Schema(description = "支付订单金额，通常为产品金额减优惠金额后的应付金额", example = "1999.00")
    private BigDecimal orderAmount;

    /** 预支付订单号 */
    @NotBlank(message = "预支付订单号不能为空")
    @Schema(description = "预支付订单号，来自预支付订单管理生成的订单号", example = "YQ20260613141642123456")
    private String prepayOrderNo;
}
