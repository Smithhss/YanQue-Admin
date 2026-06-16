package cn.yanque.models.exam.question.pojo.vo.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 修改题目状态请求。
 */
@Data
public class ExamQuestionStatusReq {

    @NotBlank(message = "状态不能为空")
    private String status;
}
