package cn.yanque.models.teaching.clazz.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzCreateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzPageReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzUpdateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.res.*;
import cn.yanque.models.teaching.clazz.service.ClazzService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 班级管理接口。
 * <p>
 * 班级关联班主任、校区和课程，前端展示时使用后端返回的名称字段。
 */
@RestController
@RequestMapping("/api/classes")
@Tag(name = "ClazzController", description = "班级管理")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 新增班级。
     *
     * @param req 班级新增请求
     * @return 新增后的班级ID
     */
    @PostMapping
    @Operation(description = "添加班级")
    public ApiResponse<ClazzCreateRes> addClazz(@Valid @RequestBody ClazzCreateReq req) {
        return ApiResponse.success(clazzService.addClazz(req));
    }

    /**
     * 修改班级。
     *
     * @param id  班级ID
     * @param req 班级修改请求
     * @return 被修改的班级ID
     */
    @PutMapping("{id:\\d+}")
    @Operation(description = "修改班级")
    public ApiResponse<ClazzUpdateRes> updateClazz(@Parameter(description = "班级ID") @PathVariable Long id,
                                                   @Valid @RequestBody ClazzUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(clazzService.updateClazz(req));
    }

    /**
     * 删除班级。
     *
     * @param id 班级ID
     * @return 被删除的班级ID
     */
    @DeleteMapping("{id:\\d+}")
    @Operation(description = "删除班级")
    public ApiResponse<ClazzDeleteRes> deleteClazz(@Parameter(description = "班级ID") @PathVariable Long id) {
        return ApiResponse.success(clazzService.deleteClazz(id));
    }

    /**
     * 根据ID查询班级。
     *
     * @param id 班级ID
     * @return 班级详情，包含班主任、校区、课程名称
     */
    @GetMapping("{id:\\d+}")
    @Operation(description = "根据ID查询班级")
    public ApiResponse<ClazzDetailRes> getClazzById(@Parameter(description = "班级ID") @PathVariable Long id) {
        return ApiResponse.success(clazzService.getClazzById(id));
    }

    /**
     * 分页查询班级。
     *
     * @param req 分页和筛选条件
     * @return 班级分页结果，包含班主任、校区、课程名称
     */
    @GetMapping
    @Operation(description = "分页查询班级")
    public ApiResponse<PageResult<ClazzPageRes>> pageClazz(@Valid @ModelAttribute ClazzPageReq req) {
        return ApiResponse.success(clazzService.pageClazz(req));
    }


}
