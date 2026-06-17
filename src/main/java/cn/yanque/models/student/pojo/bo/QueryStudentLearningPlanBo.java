package cn.yanque.models.student.pojo.bo;

import lombok.Data;

/**
 * 线上学习计划分页查询条件。
 */
@Data
public class QueryStudentLearningPlanBo {

    private String studentName;

    private String studentPhone;

    private Long courseId;

    private String status;
}
