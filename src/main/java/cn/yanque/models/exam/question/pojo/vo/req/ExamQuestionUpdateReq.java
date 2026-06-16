package cn.yanque.models.exam.question.pojo.vo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 修改题目请求。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamQuestionUpdateReq extends ExamQuestionCreateReq {

    private Long id;
}
