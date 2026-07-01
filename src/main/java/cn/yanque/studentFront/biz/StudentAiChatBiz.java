package cn.yanque.studentFront.biz;

import cn.yanque.studentFront.pojo.req.ai.CreateAiChatSessionReq;
import cn.yanque.studentFront.pojo.req.ai.SendAiChatMessageReq;
import cn.yanque.studentFront.pojo.res.ai.AiChatMessageRes;
import cn.yanque.studentFront.pojo.res.ai.AiChatSessionRes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface StudentAiChatBiz {

    AiChatSessionRes createSession(CreateAiChatSessionReq req);

    List<AiChatSessionRes> listSessions();

    List<AiChatMessageRes> listMessages(Long sessionId);

    void deleteSession(Long sessionId);

    SseEmitter streamMessage(Long sessionId, SendAiChatMessageReq req);
}
