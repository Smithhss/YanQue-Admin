package cn.yanque.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PermissionTypeEnum {

    API("api"),
    BUTTON("按钮"),
    MENU("菜单");

    private final String desc;
}
