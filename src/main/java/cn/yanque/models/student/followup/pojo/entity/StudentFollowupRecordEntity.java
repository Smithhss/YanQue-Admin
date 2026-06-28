package cn.yanque.models.student.followup.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生回访记录实体,对应 student_followup_record 表。
 */
@Data
public class StudentFollowupRecordEntity {

    private Long id;

    private Long studentId;

    private Long learningPlanId;

    private String studentTag;

    private Long followupTagId;

    private Date enrollDate;

    private Date lastFollowupTime;

    private Date dueDate;

    private Integer followupIntervalDays;

    private String status;

    private Long followupUserId;

    private Date followupTime;

    private String followupContent;

    private String followupVideoObjectKey;

    private String followupVideoFileName;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
