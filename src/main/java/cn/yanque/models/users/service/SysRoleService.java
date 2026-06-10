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

public interface SysRoleService {

    RoleCreateRes addRole(RoleCreateReq req);

    RoleUpdateRes updateRole(RoleUpdateReq req);

    RoleDeleteRes deleteRole(Long id);

    RoleDetailRes getRoleById(Long id);

    PageResult<RolePageRes> pageRole(RolePageReq req);

    RolePermissionAssignRes assignRolePermissions(Long roleId, RolePermissionAssignReq req);
}
