package cn.yanque.models.teaching.attendance.pojo.vo.req;

import cn.yanque.common.annotations.EnumValue;
import cn.yanque.models.teaching.attendance.enums.AttendanceStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceCommitItem {

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotBlank(message = "考勤状态不能为空")
    @EnumValue(enumClass = AttendanceStatusEnum.class, message = "考勤状态不合法")
    private String status;

    private String leaveReason;
}
