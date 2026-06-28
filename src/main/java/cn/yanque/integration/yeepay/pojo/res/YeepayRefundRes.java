package cn.yanque.integration.yeepay.pojo.res;

import lombok.Data;

@Data
public class YeepayRefundRes {

    private String code;

    private String message;

    /** 易宝退款流水号,字段名以易宝返回为准。 */
    private String uniqueRefundNo;

    private String refundRequestId;

    private String status;
}
