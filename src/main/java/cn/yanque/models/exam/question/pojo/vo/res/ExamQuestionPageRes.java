package cn.yanque.models.exam.question.pojo.vo.res;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 题库分页响应。
 */
@Data
public class ExamQuestionPageRes {

    private Long id;

    private String questionType;

    private String questionContent;

    private String difficulty;

    private List<Long> courseIds;

    private String courseNames;

    private List<String> courseStageKeys;

    private String courseStageNames;

    private String status;

    private Date createdAt;

    private Date updatedAt;
}
