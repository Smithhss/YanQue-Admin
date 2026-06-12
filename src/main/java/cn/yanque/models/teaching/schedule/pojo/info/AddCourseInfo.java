package cn.yanque.models.teaching.schedule.pojo.info;

import lombok.Data;

import java.util.Date;

@Data
public class AddCourseInfo {

    private String courseContent;

    private Date scheduleDate;

    private Long teacherId;
}
