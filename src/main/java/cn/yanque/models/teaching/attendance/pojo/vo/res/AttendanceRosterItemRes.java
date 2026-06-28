package cn.yanque.models.teaching.attendance.pojo.vo.res;

import lombok.Data;

@Data
public class AttendanceRosterItemRes {

    private Long studentId;

    private String studentName;

    private String studentNo;

    /** 已点名状态;未点名为 null */
    private String status;

    private String leaveReason;
}
