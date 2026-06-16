package cn.yanque.models.exam.question.pojo.vo.res;

import lombok.Data;

/**
 * 题目选项响应。
 */
@Data
public class ExamQuestionOptionRes {

    private Long id;

    private Long questionId;

    private String optionKey;

    private String optionContent;

}
