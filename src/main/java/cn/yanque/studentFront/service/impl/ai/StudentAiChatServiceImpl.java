package cn.yanque.studentFront.service.impl.ai;

import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.ai.chat.mapper.AiChatMessageMapper;
import cn.yanque.models.ai.chat.mapper.AiChatSessionMapper;
import cn.yanque.models.ai.chat.pojo.entity.AiChatMessageEntity;
import cn.yanque.models.ai.chat.pojo.entity.AiChatSessionEntity;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatMessage;
import cn.yanque.studentFront.pojo.req.ai.CreateAiChatSessionReq;
import cn.yanque.studentFront.pojo.res.ai.AiChatMessageRes;
import cn.yanque.studentFront.pojo.res.ai.AiChatSessionRes;
import cn.yanque.studentFront.service.ai.StudentAiChatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StudentAiChatServiceImpl implements StudentAiChatService {

    private static final String SESSION_STATUS_ACTIVE = "ACTIVE";

    private static final String ROLE_USER = "user";

    private static final String ROLE_ASSISTANT = "assistant";

    private static final int DEFAULT_HISTORY_LIMIT = 20;

    @Autowired
    private AiChatSessionMapper aiChatSessionMapper;

    @Autowired
    private AiChatMessageMapper aiChatMessageMapper;

    @Override
    public AiChatSessionRes createSession(Long studentId, CreateAiChatSessionReq req) {
        Date now = new Date();
        AiChatSessionEntity session = new AiChatSessionEntity();
        session.setStudentId(studentId);
        session.setTitle(normalizeTitle(req == null ? null : req.getTitle()));
        session.setStatus(SESSION_STATUS_ACTIVE);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        aiChatSessionMapper.insert(session);
        return buildSessionRes(session);
    }

    @Override
    public List<AiChatSessionRes> listSessions(Long studentId) {
        return aiChatSessionMapper.selectByStudentId(studentId).stream()
                .map(this::buildSessionRes)
                .toList();
    }

    @Override
    public List<AiChatMessageRes> listMessages(Long studentId, Long sessionId) {
        validateSession(studentId, sessionId);
        return aiChatMessageMapper.selectBySessionId(sessionId).stream()
                .map(this::buildMessageRes)
                .toList();
    }

    @Override
    public void deleteSession(Long studentId, Long sessionId) {
        validateSession(studentId, sessionId);
        aiChatSessionMapper.softDelete(sessionId, studentId);
    }

    @Override
    public void saveUserMessage(Long studentId, Long sessionId, String content) {
        validateSession(studentId, sessionId);
        AiChatMessageEntity message = buildMessage(sessionId, ROLE_USER, content, null, null);
        aiChatMessageMapper.insert(message);
        aiChatSessionMapper.updateTitleIfBlank(sessionId, studentId, buildTitle(content));
        aiChatSessionMapper.touch(sessionId, studentId);
    }

    @Override
    public void saveAssistantMessage(Long studentId, Long sessionId, String content, String model, Integer tokens) {
        validateSession(studentId, sessionId);
        AiChatMessageEntity message = buildMessage(sessionId, ROLE_ASSISTANT, content, model, tokens);
        aiChatMessageMapper.insert(message);
        aiChatSessionMapper.touch(sessionId, studentId);
    }

    @Override
    public List<PythonAiChatMessage> buildRecentHistory(Long studentId, Long sessionId, Integer limit) {
        validateSession(studentId, sessionId);
        int historyLimit = limit == null || limit <= 0 ? DEFAULT_HISTORY_LIMIT : limit;
        return aiChatMessageMapper.selectLatestBySessionId(sessionId, historyLimit).stream()
                .map(message -> new PythonAiChatMessage(message.getRole(), message.getContent()))
                .toList();
    }

    private AiChatSessionEntity validateSession(Long studentId, Long sessionId) {
        AiChatSessionEntity session = aiChatSessionMapper.selectByIdAndStudentId(sessionId, studentId);
        if (session == null) {
            throw BusinessException.DateError.newInstance("AI会话不存在");
        }
        return session;
    }

    private AiChatMessageEntity buildMessage(Long sessionId, String role, String content, String model, Integer tokens) {
        AiChatMessageEntity message = new AiChatMessageEntity();
        message.setSessionId(sessionId);
        message.setRole(role);
        message.setContent(content.trim());
        message.setModel(model);
        message.setTokens(tokens);
        message.setCreatedAt(new Date());
        return message;
    }

    private AiChatSessionRes buildSessionRes(AiChatSessionEntity session) {
        AiChatSessionRes res = new AiChatSessionRes();
        BeanUtils.copyProperties(session, res);
        return res;
    }

    private AiChatMessageRes buildMessageRes(AiChatMessageEntity message) {
        AiChatMessageRes res = new AiChatMessageRes();
        BeanUtils.copyProperties(message, res);
        return res;
    }

    private String normalizeTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        return truncate(title.trim(), 100);
    }

    private String buildTitle(String content) {
        return truncate(content.trim(), 30);
    }

    private String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
