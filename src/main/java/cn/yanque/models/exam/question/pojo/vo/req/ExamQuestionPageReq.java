package cn.yanque.models.exam.question.pojo.vo.req;

import lombok.Data;

/**
 * 题库分页查询请求。
 */
@Data
public class ExamQuestionPageReq {

    private String questionType;

    private Long courseId;

    private String difficulty;

    private String status;

    private String keyword;

    private Integer pageNum;

    private Integer pageSize;
}
