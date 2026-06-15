package cn.yanque.models.order.refund.pojo.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "申请退款请求")
public class RefundApplyReq {

    @NotBlank(message = "支付订单号不能为空")
    @Schema(description = "原支付订单号")
    private String paymentOrderNo;

    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Size(max = 200, message = "退款原因不能超过200个字符")
    @Schema(description = "退款原因")
    private String reason;
}
