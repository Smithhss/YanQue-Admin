package cn.yanque.models.order.prepay.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 修改预支付订单请求对象。
 */
@Data
@Schema(description = "修改预支付订单请求")
public class PrepayOrderUpdateReq {

    /** 订单ID */
    @Schema(description = "订单ID")
    private Long id;

    /** 学生姓名 */
    @NotBlank(message = "学生姓名不能为空")
    @Schema(description = "学生姓名")
    private String studentName;

    /** 学生手机号 */
    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号")
    private String studentPhone;

    /** 产品ID */
    @NotNull(message = "产品ID不能为空")
    @Schema(description = "产品ID")
    private Long productId;

    /** 产品金额 */
    @NotNull(message = "产品金额不能为空")
    @DecimalMin(value = "0.00", message = "产品金额不能小于0")
    @Schema(description = "产品金额")
    private BigDecimal productAmount;

    /** 优惠金额 */
    @NotNull(message = "优惠金额不能为空")
    @DecimalMin(value = "0.00", message = "优惠金额不能小于0")
    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    /** 订单状态 */
    @NotBlank(message = "订单状态不能为空")
    @Schema(description = "订单状态：PENDING_PAYMENT待支付，PAID已支付，CANCELED已取消")
    private String orderStatus;
}
