package cn.yanque.models.order.refund.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "申请退款响应")
public class RefundApplyRes {

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "原支付订单号")
    private String paymentOrderNo;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "退款单状态")
    private String status;

    @Schema(description = "易宝退款流水号")
    private String uniqueRefundNo;
}
