package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CalendarStatusEnum {

    TODO("待办"),
    DONE("已完成");

    private final String desc;
}
