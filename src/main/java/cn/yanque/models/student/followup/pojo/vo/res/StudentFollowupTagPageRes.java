package cn.yanque.models.student.followup.pojo.vo.res;

import lombok.Data;

import java.util.Date;

@Data
public class StudentFollowupTagPageRes {

    private Long id;

    private String studentTag;

    private Integer followupIntervalDays;

    private String status;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
