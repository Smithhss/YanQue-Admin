package cn.yanque.models.student.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 线上学员每日学习日历。
 */
@Data
public class StudentLearningCalendarEntity {

    private Long id;

    private Long planId;

    private Long studentId;

    private Date studyDate;

    private String stageName;

    private Integer dayIndex;

    private Integer stageDayIndex;

    private String status;

    private Date createdAt;

    private Date updatedAt;
}
