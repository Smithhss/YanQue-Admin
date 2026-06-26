package cn.yanque.models.dorm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 床位状态。
 */
@Getter
@AllArgsConstructor
public enum DormBedStatusEnum {

    FREE("空闲"),

    OCCUPIED("占用"),

    LOCKED("锁定");

    private final String desc;
}
