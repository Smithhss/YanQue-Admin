package cn.yanque.integration.payment.pojo.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentUnifiedOrderReq {

    private String orderNo;

    private BigDecimal orderAmount;
}
