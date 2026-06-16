package cn.yanque.models.exam.question.pojo.vo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 题目详情响应。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamQuestionDetailRes extends ExamQuestionPageRes {

    private String answerContent;

    private String analysisContent;

    private List<ExamQuestionOptionRes> options;
}
