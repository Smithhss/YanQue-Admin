package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GradingStatusEnum {

    PENDING("待批改"),
    COMPLETED("已批改");

    private final String desc;
}
