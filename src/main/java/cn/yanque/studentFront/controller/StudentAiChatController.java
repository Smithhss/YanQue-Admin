package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.biz.StudentAiChatBiz;
import cn.yanque.studentFront.pojo.req.ai.CreateAiChatSessionReq;
import cn.yanque.studentFront.pojo.req.ai.SendAiChatMessageReq;
import cn.yanque.studentFront.pojo.res.ai.AiChatMessageRes;
import cn.yanque.studentFront.pojo.res.ai.AiChatSessionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 学生AI问答接口。
 */
@RestController
@RequestMapping("/student/ai-chat")
@Tag(name = "StudentAiChatController", description = "学生AI问答")
public class StudentAiChatController {

    @Autowired
    private StudentAiChatBiz studentAiChatBiz;

    @PostMapping("sessions")
    @Operation(description = "创建AI问答会话")
    public ApiResponse<AiChatSessionRes> createSession(@RequestBody(required = false) CreateAiChatSessionReq req) {
        return ApiResponse.success(studentAiChatBiz.createSession(req));
    }

    @GetMapping("sessions")
    @Operation(description = "查询我的AI问答会话")
    public ApiResponse<List<AiChatSessionRes>> listSessions() {
        return ApiResponse.success(studentAiChatBiz.listSessions());
    }

    @GetMapping("sessions/{sessionId}/messages")
    @Operation(description = "查询AI问答消息")
    public ApiResponse<List<AiChatMessageRes>> listMessages(@PathVariable Long sessionId) {
        return ApiResponse.success(studentAiChatBiz.listMessages(sessionId));
    }

    @DeleteMapping("sessions/{sessionId}")
    @Operation(description = "删除AI问答会话")
    public ApiResponse<Void> deleteSession(@PathVariable Long sessionId) {
        studentAiChatBiz.deleteSession(sessionId);
        return ApiResponse.success();
    }

    @PostMapping("sessions/{sessionId}/messages/stream")
    @Operation(description = "发送AI问答消息并流式返回")
    public SseEmitter streamMessage(@PathVariable Long sessionId,
                                    @Valid @RequestBody SendAiChatMessageReq req) {
        return studentAiChatBiz.streamMessage(sessionId, req);
    }
}
