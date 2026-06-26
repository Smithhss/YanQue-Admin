package cn.yanque.models.dorm.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormBedCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBedRes;
import cn.yanque.models.dorm.service.DormBedService;
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
 * 宿舍床位管理接口。
 */
@RestController
@RequestMapping("/api/dorm/bed")
@Tag(name = "DormBedController", description = "宿舍床位管理")
public class DormBedController {

    @Autowired
    private DormBedService dormBedService;

    @PostMapping
    @Operation(description = "手动添加床位")
    public ApiResponse<Long> addBed(@Valid @RequestBody DormBedCreateReq req) {
        return ApiResponse.success(dormBedService.addBed(req));
    }

    @PutMapping("{id}")
    @Operation(description = "修改床位")
    public ApiResponse<Long> updateBed(@Parameter(description = "床位ID") @PathVariable Long id,
                                       @Valid @RequestBody DormBedUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(dormBedService.updateBed(req));
    }

    @DeleteMapping("{id}")
    @Operation(description = "删除床位")
    public ApiResponse<Long> deleteBed(@Parameter(description = "床位ID") @PathVariable Long id) {
        return ApiResponse.success(dormBedService.deleteBed(id));
    }

    @GetMapping("{id}")
    @Operation(description = "根据ID查询床位")
    public ApiResponse<DormBedRes> getBedById(@Parameter(description = "床位ID") @PathVariable Long id) {
        return ApiResponse.success(dormBedService.getBedById(id));
    }

    @GetMapping
    @Operation(description = "分页查询床位")
    public ApiResponse<PageResult<DormBedRes>> pageBed(@Valid @ModelAttribute DormBedPageReq req) {
        return ApiResponse.success(dormBedService.pageBed(req));
    }
}
