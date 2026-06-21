package cn.yanque.models.teaching.attendance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendanceStatusEnum {

    PRESENT("出勤"),

    LATE("迟到"),

    LEAVE("请假"),

    ABSENT("旷课");

    private final String desc;
}
