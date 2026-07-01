package cn.yanque.studentFront.pojo.req.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建AI问答会话请求")
public class CreateAiChatSessionReq {

    @Schema(description = "会话标题")
    private String title;
}
