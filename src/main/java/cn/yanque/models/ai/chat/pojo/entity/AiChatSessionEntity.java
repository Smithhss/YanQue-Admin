package cn.yanque.models.ai.chat.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * AI问答会话实体。
 */
@Data
public class AiChatSessionEntity {

    private Long id;

    private Long studentId;

    private String title;

    private String status;

    private Date createdAt;

    private Date updatedAt;
}
