package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ActiveEnum {

    ACTIVE("生效"),

    INACTIVE("失效");

    @Getter
    private String desc;
}
