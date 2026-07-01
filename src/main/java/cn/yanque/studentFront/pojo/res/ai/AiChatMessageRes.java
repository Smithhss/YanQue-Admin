package cn.yanque.studentFront.pojo.res.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "AI问答消息响应")
public class AiChatMessageRes {

    private Long id;

    private Long sessionId;

    private String role;

    private String content;

    private String model;

    private Integer tokens;

    private Date createdAt;
}
