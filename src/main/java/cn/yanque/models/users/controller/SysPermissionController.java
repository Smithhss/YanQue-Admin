package cn.yanque.models.users.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.users.pojo.vo.req.PermissionCreateReq;
import cn.yanque.models.users.pojo.vo.req.PermissionPageReq;
import cn.yanque.models.users.pojo.vo.req.PermissionUpdateReq;
import cn.yanque.models.users.pojo.vo.res.PermissionCreateRes;
import cn.yanque.models.users.pojo.vo.res.PermissionDeleteRes;
import cn.yanque.models.users.pojo.vo.res.PermissionDetailRes;
import cn.yanque.models.users.pojo.vo.res.PermissionPageRes;
import cn.yanque.models.users.pojo.vo.res.PermissionUpdateRes;
import cn.yanque.models.users.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sysPermission")
@Slf4j
@Tag(name = "SysPermissionController", description = "系统权限管理")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    @PostMapping
    @Operation(description = "添加权限")
    public ApiResponse<PermissionCreateRes> addPermission(@Valid @RequestBody PermissionCreateReq req) {

        return ApiResponse.success(sysPermissionService.addPermission(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改权限")
    public ApiResponse<PermissionUpdateRes> updatePermission(@Parameter(description = "权限ID") @PathVariable Long id,
                                                             @Valid @RequestBody PermissionUpdateReq req) {

        req.setId(id);
        return ApiResponse.success(sysPermissionService.updatePermission(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除权限")
    public ApiResponse<PermissionDeleteRes> deletePermission(@Parameter(description = "权限ID") @PathVariable Long id) {

        return ApiResponse.success(sysPermissionService.deletePermission(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询权限")
    public ApiResponse<PermissionDetailRes> getPermissionById(@Parameter(description = "权限ID") @PathVariable Long id) {

        return ApiResponse.success(sysPermissionService.getPermissionById(id));
    }

    @GetMapping
    @Operation(description = "分页查询权限")
    public ApiResponse<PageResult<PermissionPageRes>> pagePermission(@Valid @ModelAttribute PermissionPageReq req) {

        return ApiResponse.success(sysPermissionService.pagePermission(req));
    }
}
