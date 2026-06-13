package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OrderStatusEnum {

    INIT("初始化"),
    FAIL("失败"),
    PROCESSING("支付中"),
    SUCCESS("支付成功"),
    TIMEOUT("超时"),

    ;

    @Getter
    private final String desc;

}
