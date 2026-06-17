package cn.yanque.models.student.followup.pojo.bo;

import lombok.Data;

@Data
public class StudentFollowupRecordStatsBo {

    private Long totalCount;

    private Long todayNeedFollowupCount;

    private Long todayFollowedCount;

    private Long completedCount;
}
