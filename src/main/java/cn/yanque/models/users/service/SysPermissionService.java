package cn.yanque.models.users.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.users.pojo.vo.req.PermissionCreateReq;
import cn.yanque.models.users.pojo.vo.req.PermissionPageReq;
import cn.yanque.models.users.pojo.vo.req.PermissionUpdateReq;
import cn.yanque.models.users.pojo.vo.res.PermissionCreateRes;
import cn.yanque.models.users.pojo.vo.res.PermissionDeleteRes;
import cn.yanque.models.users.pojo.vo.res.PermissionDetailRes;
import cn.yanque.models.users.pojo.vo.res.PermissionPageRes;
import cn.yanque.models.users.pojo.vo.res.PermissionUpdateRes;

public interface SysPermissionService {

    PermissionCreateRes addPermission(PermissionCreateReq req);

    PermissionUpdateRes updatePermission(PermissionUpdateReq req);

    PermissionDeleteRes deletePermission(Long id);

    PermissionDetailRes getPermissionById(Long id);

    PageResult<PermissionPageRes> pagePermission(PermissionPageReq req);
}
