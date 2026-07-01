package cn.yanque.studentFront.client.ai;

import cn.yanque.config.AiChatProperties;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatDone;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatStreamReq;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class PythonAiChatClient {

    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private static final String EVENT_MESSAGE_START = "message_start";

    private static final String EVENT_CHUNK = "chunk";

    private static final String EVENT_DONE = "done";

    private static final String EVENT_ERROR = "error";

    private static final String SSE_EVENT_PREFIX = "event:";

    private static final String SSE_DATA_PREFIX = "data:";

    @Autowired
    private AiChatProperties aiChatProperties;

    public void streamChat(PythonAiChatStreamReq req, PythonAiChatStreamHandler handler) {
        try (HttpResponse response = buildStreamRequest(req).executeAsync()) {
            // Hutool 的 executeAsync() 在这里不是为了“另起线程”，而是为了拿到未被整体消费的响应流。
            // Python 返回的是 text/event-stream，如果用会一次性读取 body 的方式，前端就看不到逐步输出。
            validateResponse(response);
            // 从响应流中逐行读取 SSE，读到一个事件就立刻回调 handler，让 Java 可以继续转发给学生端。
            readSse(response, handler);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.RemoteError.newInstance("AI服务调用异常");
        }
        // 流式请求结束或异常后必须关闭连接，避免长连接占用连接资源。
    }

    private HttpRequest buildStreamRequest(PythonAiChatStreamReq req) {
        // Python 侧接口固定接收 JSON，请求体里包含 studentId/sessionId/message/history。
        // connectTimeout 控制建连时间，readTimeout 控制整次流式回答最长等待时间。
        return HttpRequest.post(buildStreamUrl())
                .contentType(CONTENT_TYPE_JSON)
                .setConnectionTimeout(toMillis(aiChatProperties.getConnectTimeoutSeconds(), 3))
                .setReadTimeout(toMillis(aiChatProperties.getRequestTimeoutSeconds(), 120))
                .body(JSON.toJSONString(req));
    }

    private void validateResponse(HttpResponse response) {
        if (response == null || !response.isOk()) {
            throw BusinessException.RemoteError.newInstance("AI服务调用失败");
        }
    }

    private void readSse(HttpResponse response, PythonAiChatStreamHandler handler) throws IOException {
        // executeAsync + bodyStream 可以边接收 Python 的 SSE 边处理，避免等完整回答返回后才输出。
        // SSE 事件格式大概是：
        // event: chunk
        // data: {"content":"一段增量文本"}
        //
        // 一个事件以空行结束，所以这里按行读取，遇到空行就分发一次事件。
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.bodyStream(), StandardCharsets.UTF_8))) {
            String event = null;
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    // 空行代表当前 SSE 事件结束，可以把已经收集到的 event/data 交给业务回调。
                    dispatchEvent(event, data.toString(), handler);
                    event = null;
                    data.setLength(0);
                    continue;
                }
                event = readEventName(line, event);
                appendEventData(line, data);
            }
            if (event != null || !data.isEmpty()) {
                // 理论上标准 SSE 会以空行结尾；这里兜底处理一下连接关闭前未分发的最后一段。
                dispatchEvent(event, data.toString(), handler);
            }
        }
    }

    private void dispatchEvent(String event, String data, PythonAiChatStreamHandler handler) {
        if (event == null || data == null || data.isBlank()) {
            return;
        }
        // Python 已经把所有事件数据统一包装成 JSON，Java 只按事件名转到对应回调。
        // 这里不做落库，落库在 biz 层完成，避免 HTTP 客户端承担业务状态处理。
        JSONObject body = JSON.parseObject(data);
        switch (event) {
            case EVENT_MESSAGE_START -> handler.onStart(body.getString("model"));
            case EVENT_CHUNK -> handler.onChunk(body.getString("content"));
            case EVENT_DONE -> handler.onDone(body.toJavaObject(PythonAiChatDone.class));
            case EVENT_ERROR -> handler.onError(body.getString("message"));
            default -> {
            }
        }
    }

    private String readEventName(String line, String currentEvent) {
        if (line.startsWith(SSE_EVENT_PREFIX)) {
            // event 行只表示事件类型，例如 chunk/done/error，不包含正文。
            return line.substring(SSE_EVENT_PREFIX.length()).trim();
        }
        return currentEvent;
    }

    private void appendEventData(String line, StringBuilder data) {
        if (!line.startsWith(SSE_DATA_PREFIX)) {
            return;
        }
        // SSE 允许一个事件有多行 data，协议语义上需要用换行拼回去。
        // 当前 Python 只发单行 JSON，但这里保留标准兼容，后续扩展不会踩坑。
        if (!data.isEmpty()) {
            data.append('\n');
        }
        data.append(line.substring(SSE_DATA_PREFIX.length()).trim());
    }

    private String buildStreamUrl() {
        String baseUrl = aiChatProperties.getBaseUrl();
        String streamPath = aiChatProperties.getStreamPath();
        // 配置里 baseUrl 和 streamPath 是否带斜杠都能兼容，避免不同环境拼出双斜杠或漏斜杠。
        if (baseUrl.endsWith("/") && streamPath.startsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1) + streamPath;
        }
        if (!baseUrl.endsWith("/") && !streamPath.startsWith("/")) {
            return baseUrl + "/" + streamPath;
        }
        return baseUrl + streamPath;
    }

    private int toMillis(Integer seconds, int defaultSeconds) {
        int safeSeconds = seconds == null || seconds <= 0 ? defaultSeconds : seconds;
        return safeSeconds * 1000;
    }
}
