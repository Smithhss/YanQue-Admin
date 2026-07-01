package cn.yanque.studentFront.biz.impl;

import cn.yanque.config.AiChatProperties;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.common.threadlocal.StudentThreadLocal;
import cn.yanque.models.student.pojo.entity.StudentEntity;
import cn.yanque.studentFront.biz.StudentAiChatBiz;
import cn.yanque.studentFront.client.ai.PythonAiChatClient;
import cn.yanque.studentFront.client.ai.PythonAiChatStreamHandler;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatDone;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatStreamReq;
import cn.yanque.studentFront.pojo.req.ai.CreateAiChatSessionReq;
import cn.yanque.studentFront.pojo.req.ai.SendAiChatMessageReq;
import cn.yanque.studentFront.pojo.res.ai.AiChatMessageRes;
import cn.yanque.studentFront.pojo.res.ai.AiChatSessionRes;
import cn.yanque.studentFront.service.ai.StudentAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class StudentAiChatBizImpl implements StudentAiChatBiz {

    @Autowired
    private AiChatProperties aiChatProperties;

    @Autowired
    private StudentAiChatService studentAiChatService;

    @Autowired
    private PythonAiChatClient pythonAiChatClient;

    @Override
    public AiChatSessionRes createSession(CreateAiChatSessionReq req) {
        return studentAiChatService.createSession(getCurrentStudentId(), req);
    }

    @Override
    public List<AiChatSessionRes> listSessions() {
        return studentAiChatService.listSessions(getCurrentStudentId());
    }

    @Override
    public List<AiChatMessageRes> listMessages(Long sessionId) {
        return studentAiChatService.listMessages(getCurrentStudentId(), sessionId);
    }

    @Override
    public void deleteSession(Long sessionId) {
        studentAiChatService.deleteSession(getCurrentStudentId(), sessionId);
    }

    @Override
    public SseEmitter streamMessage(Long sessionId, SendAiChatMessageReq req) {
        Long studentId = getCurrentStudentId();
        String message = req.getMessage().trim();
        PythonAiChatStreamReq pythonReq = buildPythonReq(studentId, sessionId, message);
        studentAiChatService.saveUserMessage(studentId, sessionId, message);

        SseEmitter emitter = new SseEmitter(aiChatProperties.getRequestTimeoutSeconds() * 1000L);
        CompletableFuture.runAsync(() -> doStream(studentId, sessionId, pythonReq, emitter));
        return emitter;
    }

    private void doStream(Long studentId, Long sessionId, PythonAiChatStreamReq pythonReq, SseEmitter emitter) {
        StringBuilder assistantContent = new StringBuilder();

        try {
            pythonAiChatClient.streamChat(pythonReq, new PythonAiChatStreamHandler() {
                private String model;

                private Integer tokens;

                @Override
                public void onStart(String model) {
                    this.model = model;
                    send(emitter, "message_start", new StreamStartPayload(model));
                }

                @Override
                public void onChunk(String content) {
                    if (content == null || content.isEmpty()) {
                        return;
                    }
                    assistantContent.append(content);
                    send(emitter, "chunk", new StreamChunkPayload(content));
                }

                @Override
                public void onDone(PythonAiChatDone done) {
                    if (done != null) {
                        this.model = done.getModel();
                        this.tokens = done.getTokens();
                    }
                    if (!assistantContent.isEmpty()) {
                        studentAiChatService.saveAssistantMessage(studentId, sessionId, assistantContent.toString(), model, tokens);
                    }
                    send(emitter, "done", done == null ? new PythonAiChatDone() : done);
                }

                @Override
                public void onError(String message) {
                    send(emitter, "error", new StreamErrorPayload(message));
                }
            });
            emitter.complete();
        } catch (Exception e) {
            send(emitter, "error", new StreamErrorPayload(e.getMessage()));
            emitter.completeWithError(e);
        }
    }

    private PythonAiChatStreamReq buildPythonReq(Long studentId, Long sessionId, String message) {
        PythonAiChatStreamReq pythonReq = new PythonAiChatStreamReq();
        pythonReq.setStudentId(studentId);
        pythonReq.setSessionId(sessionId);
        pythonReq.setMessage(message);
        pythonReq.setHistory(studentAiChatService.buildRecentHistory(studentId, sessionId, aiChatProperties.getHistoryLimit()));
        return pythonReq;
    }

    private void send(SseEmitter emitter, String event, Object data) {
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
        } catch (IOException e) {
            throw BusinessException.RemoteError.newInstance("AI问答流式输出失败");
        }
    }

    private Long getCurrentStudentId() {
        StudentEntity student = StudentThreadLocal.get();
        if (student == null || student.getId() == null) {
            throw new BusinessException(401, "学生未登录或Token缺失");
        }
        return student.getId();
    }

    private record StreamStartPayload(String model) {
    }

    private record StreamChunkPayload(String content) {
    }

    private record StreamErrorPayload(String message) {
    }
}
