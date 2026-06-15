package cn.yanque.models.teaching.homework.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 作业实体。
 */
@Data
public class HomeworkEntity {

    private Long id;

    private String title;

    private String contentObjectKey;

    private String contentFileName;

    private String answerObjectKey;

    private String answerFileName;

    private Boolean answerStudentVisible;

    private Long classId;

    private Date homeworkDate;

    private String classContent;

    private Date startTime;

    private Date deadline;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
