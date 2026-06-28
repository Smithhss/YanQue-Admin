package cn.yanque.models.dorm.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignReq;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignmentPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormTransferReq;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;
import cn.yanque.models.dorm.service.DormAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 宿舍入住管理接口。
 */
@RestController
@RequestMapping("/api/dorm/assignment")
@Tag(name = "DormAssignmentController", description = "宿舍入住管理")
public class DormAssignmentController {

    @Autowired
    private DormAssignmentService dormAssignmentService;

    @PostMapping("assign")
    @Operation(description = "入住分配")
    public ApiResponse<Long> assign(@Valid @RequestBody DormAssignReq req, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("userId");
        return ApiResponse.success(dormAssignmentService.assign(req, operatorId));
    }

    @PostMapping("{id}/checkout")
    @Operation(description = "退宿")
    public ApiResponse<Long> checkout(@Parameter(description = "入住记录ID") @PathVariable Long id) {
        return ApiResponse.success(dormAssignmentService.checkout(id));
    }

    @PostMapping("transfer")
    @Operation(description = "调宿(退旧床+入新床)")
    public ApiResponse<Long> transfer(@Valid @RequestBody DormTransferReq req, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("userId");
        return ApiResponse.success(dormAssignmentService.transfer(req, operatorId));
    }

    @GetMapping
    @Operation(description = "分页查询入住记录")
    public ApiResponse<PageResult<DormAssignmentRes>> pageAssignment(@Valid @ModelAttribute DormAssignmentPageReq req) {
        return ApiResponse.success(dormAssignmentService.pageAssignment(req));
    }
}
