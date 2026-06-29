package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {

    private String gatewayUrl;

    private String appId;

    private String appPrivateKey;

    private String alipayPublicKey;

    private String notifyUrl;

    private String returnUrl;

    private String cashierUrl;

    /** 支付成功后跳转的学生端前端地址 */
    private String studentWebReturnUrl;

    private String pagePayHttpMethod = "GET";

    private Boolean returnUrlEnabled = false;

    private String charset = "UTF-8";

    private String signType = "RSA2";

    private String format = "json";

    private String productCode = "FAST_INSTANT_TRADE_PAY";
}
