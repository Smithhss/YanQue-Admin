package cn.yanque.studentFront.client.ai;

import cn.yanque.config.AiChatProperties;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatDone;
import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatStreamReq;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class PythonAiChatClient {

    @Autowired
    private AiChatProperties aiChatProperties;

    public void streamChat(PythonAiChatStreamReq req, PythonAiChatStreamHandler handler) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(aiChatProperties.getConnectTimeoutSeconds()))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildStreamUrl()))
                .timeout(Duration.ofSeconds(aiChatProperties.getRequestTimeoutSeconds()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(req), StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<java.io.InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw BusinessException.RemoteError.newInstance("AI服务调用失败");
            }
            readSse(response, handler);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.RemoteError.newInstance("AI服务调用异常");
        }
    }

    private void readSse(HttpResponse<java.io.InputStream> response, PythonAiChatStreamHandler handler) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
            String event = null;
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    dispatchEvent(event, data.toString(), handler);
                    event = null;
                    data.setLength(0);
                    continue;
                }
                if (line.startsWith("event:")) {
                    event = line.substring("event:".length()).trim();
                } else if (line.startsWith("data:")) {
                    if (data.length() > 0) {
                        data.append('\n');
                    }
                    data.append(line.substring("data:".length()).trim());
                }
            }
            if (event != null || data.length() > 0) {
                dispatchEvent(event, data.toString(), handler);
            }
        }
    }

    private void dispatchEvent(String event, String data, PythonAiChatStreamHandler handler) {
        if (event == null || data == null || data.isBlank()) {
            return;
        }
        JSONObject body = JSON.parseObject(data);
        switch (event) {
            case "message_start" -> handler.onStart(body.getString("model"));
            case "chunk" -> handler.onChunk(body.getString("content"));
            case "done" -> handler.onDone(body.toJavaObject(PythonAiChatDone.class));
            case "error" -> handler.onError(body.getString("message"));
            default -> {
            }
        }
    }

    private String buildStreamUrl() {
        String baseUrl = aiChatProperties.getBaseUrl();
        String streamPath = aiChatProperties.getStreamPath();
        if (baseUrl.endsWith("/") && streamPath.startsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1) + streamPath;
        }
        if (!baseUrl.endsWith("/") && !streamPath.startsWith("/")) {
            return baseUrl + "/" + streamPath;
        }
        return baseUrl + streamPath;
    }
}
