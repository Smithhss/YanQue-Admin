package cn.yanque.studentFront.pojo.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PythonAiChatMessage {

    private String role;

    private String content;
}
