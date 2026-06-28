package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnableStatusEnum {

    ENABLED("启用"),
    DISABLED("停用");

    private final String desc;
}
