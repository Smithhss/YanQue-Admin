package cn.yanque.models.users.mapper;

import cn.yanque.models.users.pojo.entity.SysPermissionEntity;
import cn.yanque.models.users.pojo.bo.QueryPermissionBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统权限表数据访问接口。
 */
public interface SysPermissionMapper {

    /**
     * 插入权限。
     *
     * @param permission 权限实体
     */
    void insert(SysPermissionEntity permission);

    /**
     * 按ID更新权限。
     *
     * @param permission 权限实体
     * @return 影响行数
     */
    int updateById(SysPermissionEntity permission);

    /**
     * 按ID查询权限。
     *
     * @param id 权限ID
     * @return 权限实体
     */
    SysPermissionEntity selectById(@Param("id") Long id);

    /**
     * 按权限编码查询权限。
     *
     * @param permissionCode 权限编码
     * @return 权限实体
     */
    SysPermissionEntity selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 查询权限列表,分页由PageHelper在Service层控制。
     *
     * @param keyword        权限编码,权限名称或接口路径关键字
     * @param parentId       父级权限ID
     * @param permissionType 权限类型
     * @param status         权限状态
     * @return 权限列表
     */
    List<SysPermissionEntity> selectPage(@Param("keyword") String keyword,
                                         @Param("parentId") Long parentId,
                                         @Param("permissionType") String permissionType,
                                         @Param("status") String status);

    /**
     * 按ID删除权限。
     *
     * @param id 权限ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除角色与指定权限的关系。
     *
     * @param permissionId 权限ID
     * @return 影响行数
     */
    int deleteRolePermissions(@Param("permissionId") Long permissionId);

    /**
     * 按条件查询权限列表。
     *
     * @param queryPermissionBo 权限查询条件
     * @return 权限列表
     */
    List<SysPermissionEntity> selectList(QueryPermissionBo queryPermissionBo);
}
