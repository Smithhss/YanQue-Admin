package cn.yanque.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public static final BusinessException UserExist = new BusinessException(10001, "用户已存在");
    public static final BusinessException UserNotExist = new BusinessException(10002, "用户不存在");
    public static final BusinessException PermissionExist = new BusinessException(11001, "权限已存在");
    public static final BusinessException PermissionNotExist = new BusinessException(11002, "权限不存在");
    public static final BusinessException PasswordError = new BusinessException(11003, "密码错误");
    public static final BusinessException RoleExist = new BusinessException(12001, "角色已存在");
    public static final BusinessException RoleNotExist = new BusinessException(12002, "角色不存在");
    public static final BusinessException ConfigExist = new BusinessException(13001, "配置已存在");
    public static final BusinessException ConfigNotExist = new BusinessException(13002, "配置不存在");



    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }


    public BusinessException newInstance(String message) {
        return new BusinessException(this.getCode(), message);
    }
}
