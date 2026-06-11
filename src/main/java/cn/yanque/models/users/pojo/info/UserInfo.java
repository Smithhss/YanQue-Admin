package cn.yanque.models.users.pojo.info;

import cn.yanque.models.users.pojo.entity.SysPermissionEntity;
import cn.yanque.models.users.pojo.entity.SysRoleEntity;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfo {

    private SysUserEntity sysUserEntity;

    private List<SysRoleEntity> sysRoleEntityList = new ArrayList<>();

    private List<SysPermissionEntity> sysPermissionEntityList = new ArrayList<>();
}
