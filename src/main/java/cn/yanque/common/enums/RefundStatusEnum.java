package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RefundStatusEnum {

    INIT("初始化"),
    PROCESSING("退款处理中"),
    SUCCESS("退款成功"),
    FAIL("退款失败"),
    CLOSED("已关闭");

    @Getter
    private final String desc;
}
