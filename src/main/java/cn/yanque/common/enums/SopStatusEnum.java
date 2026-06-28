package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SopStatusEnum {

    ASSIGNED("已分配"),
    COMPLETED("已完成");

    private final String desc;
}
