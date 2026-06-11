package cn.yanque.common.dataConfig.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.dataConfig.vo.req.SysConfigCreateReq;
import cn.yanque.common.dataConfig.vo.req.SysConfigPageReq;
import cn.yanque.common.dataConfig.vo.req.SysConfigUpdateReq;
import cn.yanque.common.dataConfig.vo.res.SysConfigCreateRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigDeleteRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigDetailRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigPageRes;
import cn.yanque.common.dataConfig.vo.res.SysConfigUpdateRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/sysConfig")
@Tag(name = "SysConfigController", description = "系统配置管理")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    @PostMapping
    @Operation(description = "添加系统配置")
    public ApiResponse<SysConfigCreateRes> addConfig(@Valid @RequestBody SysConfigCreateReq req) {
        return ApiResponse.success(sysConfigService.addConfig(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改系统配置")
    public ApiResponse<SysConfigUpdateRes> updateConfig(@Parameter(description = "配置ID") @PathVariable Long id,
                                                        @Valid @RequestBody SysConfigUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(sysConfigService.updateConfig(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除系统配置")
    public ApiResponse<SysConfigDeleteRes> deleteConfig(@Parameter(description = "配置ID") @PathVariable Long id) {
        return ApiResponse.success(sysConfigService.deleteConfig(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询系统配置")
    public ApiResponse<SysConfigDetailRes> getConfigById(@Parameter(description = "配置ID") @PathVariable Long id) {
        return ApiResponse.success(sysConfigService.getConfigById(id));
    }

    @GetMapping
    @Operation(description = "分页查询系统配置")
    public ApiResponse<PageResult<SysConfigPageRes>> pageConfig(@Valid @ModelAttribute SysConfigPageReq req) {
        return ApiResponse.success(sysConfigService.pageConfig(req));
    }
}
