package cn.yanque.models.student.coursehour.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseHourChangeTypeEnum {

    CONSUME("消耗"),

    ADJUST("调整"),

    REVERT("回滚");

    private final String desc;
}
