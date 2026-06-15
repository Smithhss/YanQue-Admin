package cn.yanque.integration.yeepay.pojo.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YeepayRefundReq {

    /** 原支付订单号 */
    private String orderNo;

    /** 本系统退款订单号 */
    private String refundOrderNo;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /**
     * 原支付易宝订单号
     */
    private String uniqueOrderNo;

    /** 退款原因 */
    private String reason;
}
