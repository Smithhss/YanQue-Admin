package cn.yanque.studentFront.pojo.res;

import lombok.Data;

import java.util.Date;

@Data
public class StudentLearningCalendarFrontRes {

    private Date studyDate;

    private String stageName;

    private Integer dayIndex;

    private String status;
}
