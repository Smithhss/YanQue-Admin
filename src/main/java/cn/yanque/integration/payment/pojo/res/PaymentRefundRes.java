package cn.yanque.integration.payment.pojo.res;

import lombok.Data;

@Data
public class PaymentRefundRes {

    private String code;

    private String message;

    private String uniqueRefundNo;

    private String refundRequestId;

    private String status;
}
