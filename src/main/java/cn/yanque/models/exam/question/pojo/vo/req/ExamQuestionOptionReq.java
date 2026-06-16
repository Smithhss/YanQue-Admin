package cn.yanque.models.exam.question.pojo.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 题目选项请求。
 */
@Data
public class ExamQuestionOptionReq {

    @NotBlank(message = "选项标识不能为空")
    private String optionKey;

    @NotBlank(message = "选项内容不能为空")
    private String optionContent;

}
