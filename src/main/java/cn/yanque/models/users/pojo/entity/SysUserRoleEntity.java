package cn.yanque.models.users.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 用户角色关系实体,对应 sys_user_role 表。
 */
@Data
public class SysUserRoleEntity {

    /** 主键ID */
    private Long id;
    /** 用户ID */
    private Long userId;
    /** 角色ID */
    private Long roleId;
    /** 关联创建时间 */
    private Date createdAt;
}
