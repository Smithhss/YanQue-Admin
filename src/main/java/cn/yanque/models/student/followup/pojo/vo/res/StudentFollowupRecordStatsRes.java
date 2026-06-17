package cn.yanque.models.student.followup.pojo.vo.res;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentFollowupRecordStatsRes {

    private Long totalCount;

    private Long todayNeedFollowupCount;

    private Long todayFollowedCount;

    private Long completedCount;

    private BigDecimal completionRate;
}
