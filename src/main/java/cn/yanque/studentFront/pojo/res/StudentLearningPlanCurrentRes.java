package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.util.Date;

@Data
public class StudentLearningPlanCurrentRes {

    private Boolean hasPlan;

    private Long id;

    private String courseName;

    private Date startDate;

    private Date endDate;

    private Integer totalDays;

    private Integer currentDayIndex;

    private String todayStageName;

    private String status;
}
