package cn.yanque.models.users.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.users.pojo.vo.req.RolePermissionAssignReq;
import cn.yanque.models.users.pojo.vo.req.RoleCreateReq;
import cn.yanque.models.users.pojo.vo.req.RolePageReq;
import cn.yanque.models.users.pojo.vo.req.RoleUpdateReq;
import cn.yanque.models.users.pojo.vo.res.RoleCreateRes;
import cn.yanque.models.users.pojo.vo.res.RoleDeleteRes;
import cn.yanque.models.users.pojo.vo.res.RoleDetailRes;
import cn.yanque.models.users.pojo.vo.res.RolePageRes;
import cn.yanque.models.users.pojo.vo.res.RolePermissionAssignRes;
import cn.yanque.models.users.pojo.vo.res.RoleUpdateRes;

/**
 * 角色管理业务接口。
 * <p>
 * 角色用于承载一组权限,用户通过绑定角色获得接口权限。
 */
public interface SysRoleService {

    /**
     * 新增角色。
     *
     * @param req 角色新增请求
     * @return 新增后的角色ID
     */
    RoleCreateRes addRole(RoleCreateReq req);

    /**
     * 修改角色。
     *
     * @param req 角色修改请求,ID由Controller从路径参数写入
     * @return 被修改的角色ID
     */
    RoleUpdateRes updateRole(RoleUpdateReq req);

    /**
     * 删除角色。
     *
     * @param id 角色ID
     * @return 被删除的角色ID
     */
    RoleDeleteRes deleteRole(Long id);

    /**
     * 根据ID查询角色详情。
     *
     * @param id 角色ID
     * @return 角色详情,包含该角色拥有的权限ID
     */
    RoleDetailRes getRoleById(Long id);

    /**
     * 分页查询角色列表。
     *
     * @param req 分页和筛选条件
     * @return 角色分页结果
     */
    PageResult<RolePageRes> pageRole(RolePageReq req);

    /**
     * 给角色分配权限。
     * <p>
     * 当前实现是覆盖式分配:先删除角色原有权限关系,再插入本次提交的权限ID。
     *
     * @param roleId 角色ID
     * @param req    权限ID列表
     * @return 角色和本次分配的权限ID列表
     */
    RolePermissionAssignRes assignRolePermissions(Long roleId, RolePermissionAssignReq req);
}
