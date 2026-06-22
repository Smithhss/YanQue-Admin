package cn.yanque.models.teaching.attendance.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AttendanceRosterRes {

    private Long scheduleId;

    private Long classId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date scheduleDate;

    private String courseContent;

    private List<AttendanceRosterItemRes> items;
}
