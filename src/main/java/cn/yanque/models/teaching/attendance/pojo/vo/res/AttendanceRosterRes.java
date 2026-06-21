package cn.yanque.models.teaching.attendance.pojo.vo.res;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AttendanceRosterRes {

    private Long scheduleId;

    private Long classId;

    private Date scheduleDate;

    private String courseContent;

    private List<AttendanceRosterItemRes> items;
}
