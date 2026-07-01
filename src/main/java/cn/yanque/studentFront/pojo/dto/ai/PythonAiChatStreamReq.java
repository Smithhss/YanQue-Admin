package cn.yanque.studentFront.pojo.dto.ai;

import lombok.Data;

import java.util.List;

@Data
public class PythonAiChatStreamReq {

    private Long studentId;

    private Long sessionId;

    private String message;

    private List<PythonAiChatMessage> history;
}
