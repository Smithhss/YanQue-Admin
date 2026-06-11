package cn.yanque.models.users.pojo.info;

import cn.yanque.models.users.pojo.entity.SysPermissionEntity;
import cn.yanque.models.users.pojo.entity.SysRoleEntity;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前用户聚合信息。
 * <p>
 * 权限校验时会同时使用用户、角色、权限三部分数据。
 */
@Data
public class UserInfo {

    /** 用户基础信息 */
    private SysUserEntity sysUserEntity;

    /** 用户拥有的角色列表 */
    private List<SysRoleEntity> sysRoleEntityList = new ArrayList<>();

    /** 用户通过角色聚合出的权限列表 */
    private List<SysPermissionEntity> sysPermissionEntityList = new ArrayList<>();
}
