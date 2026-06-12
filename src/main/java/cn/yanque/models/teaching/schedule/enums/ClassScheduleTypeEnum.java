package cn.yanque.models.teaching.schedule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassScheduleTypeEnum {

    CLASS("上课"),

    SELF_STUDY("自习"),

    REST("休息"),

    HOLIDAY("放假");

    private final String desc;
}
