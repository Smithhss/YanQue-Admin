package cn.yanque.models.users.mapper;

import cn.yanque.models.users.pojo.entity.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户表数据访问接口。
 */
public interface SysUserMapper {

    /**
     * 插入用户。
     *
     * @param user 用户实体
     */
    void insert(SysUserEntity user);

    /**
     * 按ID更新用户。
     *
     * @param user 用户实体
     * @return 影响行数
     */
    int updateById(SysUserEntity user);

    /**
     * 按ID查询用户。
     *
     * @param id 用户ID
     * @return 用户实体
     */
    SysUserEntity selectById(@Param("id") Long id);

    /**
     * 按ID集合批量查询用户。
     *
     * @param ids 用户ID集合
     * @return 用户列表
     */
    List<SysUserEntity> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 按用户名查询用户，登录时使用。
     *
     * @param username 用户名
     * @return 用户实体
     */
    SysUserEntity selectByUsername(@Param("username") String username);

    /**
     * 查询用户列表，分页由PageHelper在Service层控制。
     *
     * @param keyword  用户名、昵称、真实姓名关键字
     * @param status   用户状态
     * @param roleCode 角色编码，用于筛选某个角色下的用户
     * @return 用户列表
     */
    List<SysUserEntity> selectPage(@Param("keyword") String keyword,
                                   @Param("status") String status,
                                   @Param("roleCode") String roleCode);

    /**
     * 按ID删除用户。
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 删除用户已有角色关系。
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("userId") Long userId);

    /**
     * 批量插入用户角色关系。
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     * @return 影响行数
     */
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 查询用户已绑定的角色ID。
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
