package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "yeepay")
public class YeepayProperties {

    /**
     * 支付模式：
     * mock    - 本地/开发默认，不调用易宝，只返回本地跳转地址
     * sandbox - 使用易宝 SDK 沙箱配置，需要配置沙箱商户和证书
     * prod    - 使用易宝正式配置
     */
    private String mode;

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

    /**
     * mock 模式收银台跳转地址。开发时通常指向学生端支付返回页。
     */
    private String mockCashierUrl;

    /**
     * sandbox/prod 模式下传给易宝 SDK 的配置文件路径。
     * 例如：config/yeepay/sandbox/yop_sdk_config_default.json
     */
    private String sdkConfigFile;
}
