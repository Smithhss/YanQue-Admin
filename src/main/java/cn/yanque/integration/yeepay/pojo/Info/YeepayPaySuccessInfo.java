package cn.yanque.integration.yeepay.pojo.Info;

import lombok.Data;

@Data
public class YeepayPaySuccessInfo {

    private String orderId;

    private String uniqueOrderNo;

    private String paySuccessDate;
}
