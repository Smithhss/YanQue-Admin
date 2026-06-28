package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PrepayOrderStatusEnum {

    PENDING_PAYMENT("待支付"),
    SUCCESS("已支付"),
    CANCELED("已取消");

    private final String desc;
}
