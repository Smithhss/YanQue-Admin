package cn.yanque.models.order.refund.pojo.bo;

import lombok.Data;

@Data
public class QueryRefundOrderBo {

    /** 退款订单号 */
    private String refundOrderNo;

    /** 原支付订单号 */
    private String paymentOrderNo;

    /** 退款状态 */
    private String status;
}
