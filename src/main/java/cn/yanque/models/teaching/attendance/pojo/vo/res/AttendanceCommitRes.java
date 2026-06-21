package cn.yanque.models.teaching.attendance.pojo.vo.res;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceCommitRes {

    /** 本次提交的考勤条数 */
    private Integer committedCount;

    /** 课时不足（剩余为负）的学生预警 */
    private List<AttendanceWarningRes> warnings;
}
