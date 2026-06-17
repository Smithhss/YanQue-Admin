package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    private Boolean enabled = false;

    private String adminAddresses;

    private String accessToken;

    private String appName;

    private String address;

    private String ip;

    private Integer port;

    private String logPath;

    private Integer logRetentionDays;
}
