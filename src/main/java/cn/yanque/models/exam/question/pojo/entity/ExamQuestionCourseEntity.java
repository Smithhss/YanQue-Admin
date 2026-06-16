package cn.yanque.models.exam.question.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 题目关联课程实体，对应 exam_question_course 表。
 */
@Data
public class ExamQuestionCourseEntity {

    /** 题目ID */
    private Long questionId;

    /** 课程ID */
    private Long courseId;

    /** 阶段名称 */
    private String stageName;

    /** 创建时间 */
    private Date createdAt;
}
