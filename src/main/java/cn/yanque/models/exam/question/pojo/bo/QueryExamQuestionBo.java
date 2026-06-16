package cn.yanque.models.exam.question.pojo.bo;

import lombok.Data;

/**
 * 题库分页查询条件。
 */
@Data
public class QueryExamQuestionBo {

    private String questionType;

    private Long courseId;

    private String difficulty;

    private String status;

    private String keyword;
}
