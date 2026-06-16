package cn.yanque.studentFront.pojo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 学生交卷请求。
 */
@Data
public class StudentExamSubmitReq {

    /** 答案列表 */
    @Valid
    @NotEmpty(message = "答案不能为空")
    private List<StudentExamAnswerReq> answers;
}
