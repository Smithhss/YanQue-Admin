package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExamStatusEnum {

    NOT_STARTED("未开始"),
    IN_PROGRESS("进行中"),
    SUBMITTED("已提交");

    private final String desc;
}
