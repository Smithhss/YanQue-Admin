package cn.yanque.models.dorm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别类型：用于学生性别与宿舍楼栋性别隔离校验。
 */
@Getter
@AllArgsConstructor
public enum GenderTypeEnum {

    MALE("男"),

    FEMALE("女");

    private final String desc;
}
