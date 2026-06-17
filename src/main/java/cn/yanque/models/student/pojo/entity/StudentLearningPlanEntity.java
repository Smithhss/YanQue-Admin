package cn.yanque.models.student.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 线上学员学习计划主表。
 */
@Data
public class StudentLearningPlanEntity {

    private Long id;

    private Long studentId;

    private Long courseId;

    private Long sopId;

    /** 开始学习日期，每天连续生成学习日历，不跳过周六日。 */
    private Date startDate;

    private String status;

    private Date createdAt;

    private Date updatedAt;
}
