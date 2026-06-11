package cn.yanque.models.users.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 角色权限关系实体，对应 sys_role_permission 表。
 */
@Data
public class SysRolePermissionEntity {

    /** 主键ID */
    private Long id;
    /** 角色ID */
    private Long roleId;
    /** 权限ID */
    private Long permissionId;
    /** 关联创建时间 */
    private Date createdAt;
}
