package cn.yanque.models.order.refund.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建退款订单号响应")
public class RefundCreateRes {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "原支付订单号")
    private String paymentOrderNo;

    @Schema(description = "支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "已申请退款金额，包含处理中和成功金额")
    private BigDecimal refundedAmount;

    @Schema(description = "可退金额")
    private BigDecimal refundableAmount;

    @Schema(description = "退款单状态")
    private String status;
}
