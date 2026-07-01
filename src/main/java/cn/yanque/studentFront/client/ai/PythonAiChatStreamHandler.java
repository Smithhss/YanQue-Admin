package cn.yanque.studentFront.client.ai;

import cn.yanque.studentFront.pojo.dto.ai.PythonAiChatDone;

public interface PythonAiChatStreamHandler {

    void onStart(String model);

    void onChunk(String content);

    void onDone(PythonAiChatDone done);

    void onError(String message);
}
