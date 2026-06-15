package cn.yanque.models.order.refund.pojo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RefundOrderEntity {

    private Long id;

    /** 退款订单号 */
    private String refundOrderNo;

    /** 原支付订单号 */
    private String paymentOrderNo;

    /** 原支付金额 */
    private BigDecimal paymentAmount;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款状态 */
    private String status;

    /** 退款原因 */
    private String reason;

    /** 易宝退款流水号 */
    private String uniqueRefundNo;

    /** 失败原因 */
    private String failReason;

    /** 退款成功时间 */
    private Date refundSuccessTime;

    private Date createdAt;

    private Date updatedAt;
}
