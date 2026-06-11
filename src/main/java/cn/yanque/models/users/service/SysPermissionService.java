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

/**
 * 权限管理业务接口。
 * <p>
 * 权限支持父子关系，类型包括 MENU、API、BUTTON，其中 API 权限用于接口拦截校验。
 */
public interface SysPermissionService {

    /**
     * 新增权限。
     *
     * @param req 权限新增请求
     * @return 新增后的权限ID
     */
    PermissionCreateRes addPermission(PermissionCreateReq req);

    /**
     * 修改权限。
     *
     * @param req 权限修改请求，ID由Controller从路径参数写入
     * @return 被修改的权限ID
     */
    PermissionUpdateRes updatePermission(PermissionUpdateReq req);

    /**
     * 删除权限。
     *
     * @param id 权限ID
     * @return 被删除的权限ID
     */
    PermissionDeleteRes deletePermission(Long id);

    /**
     * 根据ID查询权限详情。
     *
     * @param id 权限ID
     * @return 权限详情
     */
    PermissionDetailRes getPermissionById(Long id);

    /**
     * 分页查询权限列表。
     *
     * @param req 分页和筛选条件
     * @return 权限分页结果
     */
    PageResult<PermissionPageRes> pagePermission(PermissionPageReq req);
}
