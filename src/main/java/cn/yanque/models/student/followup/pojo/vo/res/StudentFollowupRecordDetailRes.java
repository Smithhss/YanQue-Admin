package cn.yanque.models.student.followup.pojo.vo.res;

import lombok.Data;

import java.util.Date;

@Data
public class StudentFollowupRecordDetailRes {

    private Long id;

    private Long studentId;

    private String studentName;

    private String studentPhone;

    private Long learningPlanId;

    private String studentTag;

    private Date enrollDate;

    private Date lastFollowupTime;

    private Date dueDate;

    private Integer followupIntervalDays;

    private String status;

    private Long followupUserId;

    private String followupUserName;

    private Date followupTime;

    private String followupContent;

    private String followupVideoObjectKey;

    private String followupVideoFileName;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
