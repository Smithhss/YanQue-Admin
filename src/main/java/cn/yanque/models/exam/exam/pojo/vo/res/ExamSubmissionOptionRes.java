package cn.yanque.models.exam.exam.pojo.vo.res;

import lombok.Data;

/**
 * 后台考试答卷题目选项响应。
 */
@Data
public class ExamSubmissionOptionRes {

    private Long id;

    private Long questionId;

    private String optionKey;

    private String optionContent;
}
