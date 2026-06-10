package cn.yanque.models.users.controller;

import cn.yanque.common.api.ApiResponse;
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
import cn.yanque.models.users.service.SysUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/sysUser")
@Slf4j
@Tag(name = "SysUserController", description = "系统用户管理")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping
    @Operation(description = "添加用户")
    public ApiResponse<UserCreateRes> addUser(@Valid @RequestBody UserCreateReq req){

        return ApiResponse.success(sysUserService.addUser(req));
    }


    @PutMapping("{id}")
    @Operation(description = "修改用户")
    public ApiResponse<UserUpdateRes> updateUser(@Parameter(description = "用户ID") @PathVariable Long id,
                                                 @Valid @RequestBody UserUpdateReq req){

        req.setId(id);
        return ApiResponse.success(sysUserService.updateUser(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除用户")
    public ApiResponse<UserDeleteRes> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id){

        return ApiResponse.success(sysUserService.deleteUser(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询用户")
    public ApiResponse<UserDetailRes> getUserById(@Parameter(description = "用户ID") @PathVariable Long id){

        return ApiResponse.success(sysUserService.getUserById(id));
    }

    @GetMapping
    @Operation(description = "分页查询用户")
    public ApiResponse<PageResult<UserPageRes>> pageUser(@Valid @ModelAttribute UserPageReq req){

        return ApiResponse.success(sysUserService.pageUser(req));
    }

    @PutMapping("{id}/roles")
    @Operation(description = "用户分配角色")
    public ApiResponse<UserRoleAssignRes> assignUserRoles(@Parameter(description = "用户ID") @PathVariable Long id,
                                                          @Valid @RequestBody UserRoleAssignReq req){

        return ApiResponse.success(sysUserService.assignUserRoles(id, req));
    }

}
