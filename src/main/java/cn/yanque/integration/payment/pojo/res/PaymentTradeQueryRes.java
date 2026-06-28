package cn.yanque.integration.payment.pojo.res;

import lombok.Data;

@Data
public class PaymentTradeQueryRes {

    private String tradeStatus;

    private String tradeNo;

    private String outTradeNo;
}
