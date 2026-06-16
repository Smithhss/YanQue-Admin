package cn.yanque.models.exam.exam.pojo.vo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 后台考试答卷批改请求。
 */
@Data
public class ExamSubmissionGradeReq {

    @Valid
    @NotEmpty(message = "批改题目不能为空")
    private List<ExamSubmissionGradeAnswerReq> answers;
}
