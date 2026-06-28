package cn.yanque.models.users.mapper;

import cn.yanque.models.users.pojo.entity.SysRoleEntity;
import cn.yanque.models.users.pojo.bo.QueryUserBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色表数据访问接口。
 */
public interface SysRoleMapper {

    /**
     * 插入角色。
     *
     * @param role 角色实体
     */
    void insert(SysRoleEntity role);

    /**
     * 按ID更新角色。
     *
     * @param role 角色实体
     * @return 影响行数
     */
    int updateById(SysRoleEntity role);

    /**
     * 按ID查询角色。
     *
     * @param id 角色ID
     * @return 角色实体
     */
    SysRoleEntity selectById(@Param("id") Long id);

    /**
     * 按角色编码查询角色。
     *
     * @param roleCode 角色编码
     * @return 角色实体
     */
    SysRoleEntity selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色列表,分页由PageHelper在Service层控制。
     *
     * @param keyword 角色编码或角色名称关键字
     * @param status  角色状态
     * @return 角色列表
     */
    List<SysRoleEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status);

    /**
     * 按ID删除角色。
     *
     * @param id 角色ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除角色已有权限关系。
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRolePermissions(@Param("roleId") Long roleId);

    /**
     * 删除用户与指定角色的关系。
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("roleId") Long roleId);

    /**
     * 批量插入角色权限关系。
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID集合
     * @return 影响行数
     */
    int insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 查询角色已绑定的权限ID。
     *
     * @param roleIds 角色ID集合
     * @return 权限ID集合
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleIds") List<Long> roleIds);

    /**
     * 按条件查询角色列表。
     *
     * @param queryUserBo 角色查询条件
     * @return 角色列表
     */
    List<SysRoleEntity> selectList(QueryUserBo queryUserBo);
}
