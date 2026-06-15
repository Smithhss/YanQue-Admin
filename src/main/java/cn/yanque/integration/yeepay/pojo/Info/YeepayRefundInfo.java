package cn.yanque.integration.yeepay.pojo.Info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YeepayRefundInfo {

    /** 本系统退款请求号。 */
    private String refundRequestId;

    /** 原支付订单号。 */
    private String orderId;

    /** 易宝退款流水号。 */
    private String uniqueRefundNo;

    /** 退款状态。 */
    private String status;


    private String refundSuccessDate;

    /** 失败原因。 */
    private String errorMessage;

    private String message;
}
