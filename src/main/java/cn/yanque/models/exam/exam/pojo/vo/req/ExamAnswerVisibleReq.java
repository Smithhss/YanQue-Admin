package cn.yanque.models.exam.exam.pojo.vo.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 考试答案/成绩公布状态入参。
 */
@Data
public class ExamAnswerVisibleReq {

    @NotNull(message = "公布状态不能为空")
    private Boolean answerVisible;
}
