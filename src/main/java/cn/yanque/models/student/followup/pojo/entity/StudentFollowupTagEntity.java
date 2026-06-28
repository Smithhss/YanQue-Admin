package cn.yanque.models.student.followup.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生回访标签配置实体,对应 student_followup_tag 表。
 */
@Data
public class StudentFollowupTagEntity {

    private Long id;

    private String studentTag;

    private Integer followupIntervalDays;

    private String status;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
