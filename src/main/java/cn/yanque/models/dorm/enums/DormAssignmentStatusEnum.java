package cn.yanque.models.dorm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入住记录状态。
 */
@Getter
@AllArgsConstructor
public enum DormAssignmentStatusEnum {

    LIVING("在住"),

    CHECKED_OUT("已退宿");

    private final String desc;
}
