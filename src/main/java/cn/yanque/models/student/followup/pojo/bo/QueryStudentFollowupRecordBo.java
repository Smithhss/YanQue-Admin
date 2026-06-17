package cn.yanque.models.student.followup.pojo.bo;

import lombok.Data;

import java.util.Date;

@Data
public class QueryStudentFollowupRecordBo {

    private String studentName;

    private String studentPhone;

    private String studentTag;

    private String status;

    private Long followupUserId;

    private Date startDate;

    private Date endDate;
}
