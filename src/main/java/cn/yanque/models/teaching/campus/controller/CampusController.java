package cn.yanque.models.teaching.campus.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusCreateReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusPageReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusUpdateReq;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusCreateRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDeleteRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDetailRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusPageRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusUpdateRes;
import cn.yanque.models.teaching.campus.service.CampusService;
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

/**
 * 校区管理接口。
 */
@RestController
@RequestMapping("/api/campus")
@Tag(name = "CampusController", description = "校区管理")
public class CampusController {

    @Autowired
    private CampusService campusService;

    /**
     * 新增校区。
     *
     * @param req 校区新增请求
     * @return 新增后的校区ID
     */
    @PostMapping
    @Operation(description = "添加校区")
    public ApiResponse<CampusCreateRes> addCampus(@Valid @RequestBody CampusCreateReq req) {
        return ApiResponse.success(campusService.addCampus(req));
    }

    /**
     * 修改校区。
     *
     * @param id  校区ID
     * @param req 校区修改请求
     * @return 被修改的校区ID
     */
    @PutMapping("{id}")
    @Operation(description = "修改校区")
    public ApiResponse<CampusUpdateRes> updateCampus(@Parameter(description = "校区ID") @PathVariable Long id,
                                                     @Valid @RequestBody CampusUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(campusService.updateCampus(req));
    }

    /**
     * 删除校区。
     *
     * @param id 校区ID
     * @return 被删除的校区ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "删除校区")
    public ApiResponse<CampusDeleteRes> deleteCampus(@Parameter(description = "校区ID") @PathVariable Long id) {
        return ApiResponse.success(campusService.deleteCampus(id));
    }

    /**
     * 根据ID查询校区。
     *
     * @param id 校区ID
     * @return 校区详情
     */
    @GetMapping("{id}")
    @Operation(description = "根据ID查询校区")
    public ApiResponse<CampusDetailRes> getCampusById(@Parameter(description = "校区ID") @PathVariable Long id) {
        return ApiResponse.success(campusService.getCampusById(id));
    }

    /**
     * 分页查询校区。
     *
     * @param req 分页和搜索条件
     * @return 校区分页结果
     */
    @GetMapping
    @Operation(description = "分页查询校区")
    public ApiResponse<PageResult<CampusPageRes>> pageCampus(@Valid @ModelAttribute CampusPageReq req) {
        return ApiResponse.success(campusService.pageCampus(req));
    }
}
