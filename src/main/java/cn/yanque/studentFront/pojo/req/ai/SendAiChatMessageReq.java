package cn.yanque.studentFront.pojo.req.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "发送AI问答消息请求")
public class SendAiChatMessageReq {

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "用户消息内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}
