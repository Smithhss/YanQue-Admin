package cn.yanque.studentFront.service.ai;

import cn.yanque.studentFront.pojo.req.ai.CreateAiChatSessionReq;
import cn.yanque.studentFront.pojo.res.ai.AiChatMessageRes;
import cn.yanque.studentFront.pojo.res.ai.AiChatSessionRes;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatMessage;

import java.util.List;

public interface StudentAiChatService {

    AiChatSessionRes createSession(Long studentId, CreateAiChatSessionReq req);

    List<AiChatSessionRes> listSessions(Long studentId);

    List<AiChatMessageRes> listMessages(Long studentId, Long sessionId);

    void deleteSession(Long studentId, Long sessionId);

    void saveUserMessage(Long studentId, Long sessionId, String content);

    void saveAssistantMessage(Long studentId, Long sessionId, String content, String model, Integer tokens);

    List<PythonAiChatMessage> buildRecentHistory(Long studentId, Long sessionId, Integer limit);
}
