package cn.yanque.studentFront.pojo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 学生考试单题答案请求。
 */
@Data
public class StudentExamAnswerReq {

    /** 题目ID */
    @NotNull(message = "题目ID不能为空")
    private Long questionId;

    /** 学生答案内容 */
    private String answerContent;
}
