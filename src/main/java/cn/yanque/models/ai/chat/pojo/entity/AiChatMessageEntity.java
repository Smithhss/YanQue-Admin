package cn.yanque.models.ai.chat.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI问答消息实体。
 */
@Data
public class AiChatMessageEntity {

    private Long id;

    private Long sessionId;

    private String role;

    private String content;

    private String model;

    private Integer tokens;

    private Date createdAt;
}
