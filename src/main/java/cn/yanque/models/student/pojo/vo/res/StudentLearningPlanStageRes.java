package cn.yanque.models.student.pojo.vo.res;

import lombok.Data;

@Data
public class StudentLearningPlanStageRes {

    private Long id;

    private Long planId;

    private String stageName;

    private Integer stageDays;

    private Integer sortOrder;
}
