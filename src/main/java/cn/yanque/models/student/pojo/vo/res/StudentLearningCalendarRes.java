package cn.yanque.models.student.pojo.vo.res;

import lombok.Data;

import java.util.Date;

@Data
public class StudentLearningCalendarRes {

    private Long id;

    private Long planId;

    private Long studentId;

    private Date studyDate;

    private String stageName;

    private Integer dayIndex;

    private Integer stageDayIndex;

    private String status;
}
