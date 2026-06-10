package cn.yanque.models.users.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.users.pojo.vo.req.UserCreateReq;
import cn.yanque.models.users.pojo.vo.req.UserPageReq;
import cn.yanque.models.users.pojo.vo.req.UserRoleAssignReq;
import cn.yanque.models.users.pojo.vo.req.UserUpdateReq;
import cn.yanque.models.users.pojo.vo.res.UserCreateRes;
import cn.yanque.models.users.pojo.vo.res.UserDeleteRes;
import cn.yanque.models.users.pojo.vo.res.UserDetailRes;
import cn.yanque.models.users.pojo.vo.res.UserPageRes;
import cn.yanque.models.users.pojo.vo.res.UserRoleAssignRes;
import cn.yanque.models.users.pojo.vo.res.UserUpdateRes;

public interface SysUserService {
    UserCreateRes addUser(UserCreateReq req);

    UserUpdateRes updateUser(UserUpdateReq req);

    UserDeleteRes deleteUser(Long id);

    UserDetailRes getUserById(Long id);

    PageResult<UserPageRes> pageUser(UserPageReq req);

    UserRoleAssignRes assignUserRoles(Long userId, UserRoleAssignReq req);
}
