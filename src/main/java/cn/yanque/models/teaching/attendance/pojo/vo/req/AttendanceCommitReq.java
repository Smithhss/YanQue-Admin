package cn.yanque.models.teaching.attendance.pojo.vo.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AttendanceCommitReq {

    @NotNull(message = "课次ID不能为空")
    private Long scheduleId;

    @NotEmpty(message = "考勤名单不能为空")
    @Valid
    private List<AttendanceCommitItem> items;
}
