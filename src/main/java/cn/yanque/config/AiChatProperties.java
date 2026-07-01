package cn.yanque.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ai-chat")
public class AiChatProperties {

    /**
     * Python AI服务基础地址。
     */
    private String baseUrl = "http://127.0.0.1:8000";

    /**
     * 流式问答接口路径。
     */
    private String streamPath = "/api/ai-chat/stream";

    /**
     * HTTP连接超时时间，单位秒。
     */
    private Integer connectTimeoutSeconds = 3;

    /**
     * 单次流式问答超时时间，单位秒。
     */
    private Integer requestTimeoutSeconds = 120;

    /**
     * 发送给Python的最近历史消息数量。
     */
    private Integer historyLimit = 20;
}
