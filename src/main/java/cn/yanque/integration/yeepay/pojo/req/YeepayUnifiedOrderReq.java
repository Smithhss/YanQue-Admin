package cn.yanque.integration.yeepay.pojo.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YeepayUnifiedOrderReq {

    private String orderNo;

    private BigDecimal OrderAmount;

}
