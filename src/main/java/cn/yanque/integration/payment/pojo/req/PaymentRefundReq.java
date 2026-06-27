package cn.yanque.integration.payment.pojo.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRefundReq {

    private String orderNo;

    private String refundOrderNo;

    private BigDecimal refundAmount;

    private String uniqueOrderNo;

    private String reason;
}
