package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "yeepay")
public class YeepayProperties {

    /**
     * 业务接入商编。
     */
    private String parentMerchantNo;

    /**
     * 收款商编。
     */
    private String merchantNo;

    /**
     * 支付成功回调地址
     */
    private String paySuccessNotifyUrl;

    /**
     * 支付成功跳转地址
     */
    private String paySuccessReturnUrl;

    /**
     * 退款结果回调地址
     */
    private String refundNotifyUrl;
}
