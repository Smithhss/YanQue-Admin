package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FollowupStatusEnum {

    NEED_FOLLOWUP("需跟进"),
    COMPLETED("已跟进");

    private final String desc;
}
