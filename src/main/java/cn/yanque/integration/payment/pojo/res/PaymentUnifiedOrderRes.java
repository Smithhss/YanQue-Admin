package cn.yanque.integration.payment.pojo.res;

import lombok.Data;

@Data
public class PaymentUnifiedOrderRes {

    private String uniqueOrderNo;

    private String cashierUrl;
}
