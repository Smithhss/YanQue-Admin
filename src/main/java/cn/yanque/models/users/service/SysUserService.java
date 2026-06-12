package cn.yanque.models.users.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.users.pojo.entity.SysUserEntity;
import cn.yanque.models.users.pojo.info.UserInfo;
import cn.yanque.models.users.pojo.vo.req.*;
import cn.yanque.models.users.pojo.vo.res.*;

import java.util.List;

/**
 * 用户管理业务接口。
 * <p>
 * 包含用户增删改查、登录、用户分配角色，以及拦截器中使用的用户信息聚合查询。
 */
public interface SysUserService {

    /**
     * 新增用户。
     *
     * @param req 用户新增请求
     * @return 新增后的用户ID
     */
    UserCreateRes addUser(UserCreateReq req);

    /**
     * 修改用户基础信息。
     *
     * @param req 用户修改请求，ID由Controller从路径参数写入
     * @return 被修改的用户ID
     */
    UserUpdateRes updateUser(UserUpdateReq req);

    /**
     * 删除用户。
     *
     * @param id 用户ID
     * @return 被删除的用户ID
     */
    UserDeleteRes deleteUser(Long id);

    /**
     * 根据ID查询用户详情。
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserDetailRes getUserById(Long id);

    /**
     * 分页查询用户列表。
     *
     * @param req 分页和筛选条件
     * @return 用户分页结果
     */
    PageResult<UserPageRes> pageUser(UserPageReq req);

    /**
     * 给用户分配角色。
     * <p>
     * 当前实现是覆盖式分配：先删除用户原有角色关系，再插入本次提交的角色ID。
     *
     * @param userId 用户ID
     * @param req    角色ID列表
     * @return 用户和本次分配的角色ID列表
     */
    UserRoleAssignRes assignUserRoles(Long userId, UserRoleAssignReq req);

    /**
     * 用户登录。
     *
     * @param req 登录请求
     * @return token、签名密钥、用户信息、角色和权限
     */
    LoginRes login(LoginReq req);

    /**
     * 查询当前用户的聚合信息。
     * <p>
     * 权限拦截器会使用这个方法获取用户、角色、权限列表。
     *
     * @param id 用户ID
     * @return 用户聚合信息
     */
    UserInfo getUserInfo(Long id);

    /**
     * 根据角色查询用户信息
     */
    List<SysUserEntity> getUserByRoleCode(String roleCode);
}
