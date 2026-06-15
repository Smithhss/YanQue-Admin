package cn.yanque.models.order.refund.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "退款订单分页项")
public class RefundOrderPageRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款订单号")
    private String refundOrderNo;

    @Schema(description = "原支付订单号")
    private String paymentOrderNo;

    @Schema(description = "原支付金额")
    private BigDecimal paymentAmount;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "退款原因")
    private String reason;

    @Schema(description = "易宝退款流水号")
    private String uniqueRefundNo;

    @Schema(description = "失败原因")
    private String failReason;

    @Schema(description = "退款成功时间")
    private Date refundSuccessTime;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;
}
