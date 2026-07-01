package cn.yanque.studentFront.pojo.res.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "AI问答会话响应")
public class AiChatSessionRes {

    private Long id;

    private String title;

    private String status;

    private Date createdAt;

    private Date updatedAt;
}
