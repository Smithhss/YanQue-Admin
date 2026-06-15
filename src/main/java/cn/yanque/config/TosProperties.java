package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tos")
public class TosProperties {

    /**
     * TOS 服务地址。
     */
    private String endpoint;

    /**
     * TOS 区域。
     */
    private String region;

    /**
     * TOS 桶名称。
     */
    private String bucket;

    /**
     * TOS 访问密钥。
     */
    private String accessKey;

    /**
     * TOS 密钥。
     */
    private String secretKey;

}
