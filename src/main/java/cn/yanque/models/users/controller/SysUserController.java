package cn.yanque.models.users.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.models.users.pojo.vo.req.UserCreateReq;
import cn.yanque.models.users.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("addUser")
    @Operation(description = "添加用户")
    public ApiResponse<Void> addUser(@RequestBody UserCreateReq req){
        sysUserService.addUser(req);

        return ApiResponse.success();
    }


    @PostMapping("updateUser")
    public ApiResponse<Void> updateUser(@RequestBody UserCreateReq req){

        sysUserService.addUser(req);
        return ApiResponse.success();
    }




}
