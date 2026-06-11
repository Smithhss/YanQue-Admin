package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StageNameEnum {

    BASIC("基础"),

    SPRING("spring"),

    PYTHON("python"),

    AI("ai");

    @Getter
    private final String desc;
}
