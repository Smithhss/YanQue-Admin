package cn.yanque.models.teaching.homework.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生作业提交记录。
 */
@Data
public class HomeworkSubmissionEntity {

    private Long id;

    private Long homeworkId;

    private Long studentId;

    private Long classId;

    private String contentObjectKey;

    private String contentFileName;

    private Date submitTime;

    private Boolean lateSubmitted;

    private String teacherRemark;

    private Integer score;

    private Date createdAt;

    private Date updatedAt;
}
