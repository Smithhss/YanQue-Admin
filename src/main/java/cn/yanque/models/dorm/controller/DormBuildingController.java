package cn.yanque.models.dorm.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBuildingRes;
import cn.yanque.models.dorm.service.DormBuildingService;
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
 * 宿舍楼栋管理接口。
 */
@RestController
@RequestMapping("/api/dorm/building")
@Tag(name = "DormBuildingController", description = "宿舍楼栋管理")
public class DormBuildingController {

    @Autowired
    private DormBuildingService dormBuildingService;

    @PostMapping
    @Operation(description = "添加宿舍楼栋")
    public ApiResponse<Long> addBuilding(@Valid @RequestBody DormBuildingCreateReq req) {
        return ApiResponse.success(dormBuildingService.addBuilding(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改宿舍楼栋")
    public ApiResponse<Long> updateBuilding(@Parameter(description = "楼栋ID") @PathVariable Long id,
                                            @Valid @RequestBody DormBuildingUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(dormBuildingService.updateBuilding(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除宿舍楼栋")
    public ApiResponse<Long> deleteBuilding(@Parameter(description = "楼栋ID") @PathVariable Long id) {
        return ApiResponse.success(dormBuildingService.deleteBuilding(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询宿舍楼栋")
    public ApiResponse<DormBuildingRes> getBuildingById(@Parameter(description = "楼栋ID") @PathVariable Long id) {
        return ApiResponse.success(dormBuildingService.getBuildingById(id));
    }

    @GetMapping
    @Operation(description = "分页查询宿舍楼栋")
    public ApiResponse<PageResult<DormBuildingRes>> pageBuilding(@Valid @ModelAttribute DormBuildingPageReq req) {
        return ApiResponse.success(dormBuildingService.pageBuilding(req));
    }
}
