package cn.yanque.models.order.prepay.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderCreateReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.req.PrepayOrderUpdateReq;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderCreateRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDeleteRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderDetailRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderPageRes;
import cn.yanque.models.order.prepay.pojo.vo.res.PrepayOrderUpdateRes;
import cn.yanque.models.order.prepay.service.PrepayOrderService;
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
 * 预支付订单管理接口。
 */
@RestController
@RequestMapping("/api/prepayOrders")
@Tag(name = "PrepayOrderController", description = "预支付订单管理")
public class PrepayOrderController {

    @Autowired
    private PrepayOrderService prepayOrderService;

    /**
     * 新增预支付订单。
     *
     * @param req 预支付订单新增请求
     * @return 新增后的订单ID
     */
    @PostMapping
    @Operation(description = "添加预支付订单")
    public ApiResponse<PrepayOrderCreateRes> addPrepayOrder(@Valid @RequestBody PrepayOrderCreateReq req) {
        return ApiResponse.success(prepayOrderService.addPrepayOrder(req));
    }

    /**
     * 修改预支付订单。
     *
     * @param id  订单ID
     * @param req 预支付订单修改请求
     * @return 被修改的订单ID
     */
    @PutMapping("{id}")
    @Operation(description = "修改预支付订单")
    public ApiResponse<PrepayOrderUpdateRes> updatePrepayOrder(@Parameter(description = "订单ID") @PathVariable Long id,
                                                               @Valid @RequestBody PrepayOrderUpdateReq req) {
        req.setId(id);
        return ApiResponse.success(prepayOrderService.updatePrepayOrder(req));
    }

    /**
     * 删除预支付订单。
     *
     * @param id 订单ID
     * @return 被删除的订单ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "删除预支付订单")
    public ApiResponse<PrepayOrderDeleteRes> deletePrepayOrder(@Parameter(description = "订单ID") @PathVariable Long id) {
        return ApiResponse.success(prepayOrderService.deletePrepayOrder(id));
    }

    /**
     * 根据ID查询预支付订单。
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("{id}")
    @Operation(description = "根据ID查询预支付订单")
    public ApiResponse<PrepayOrderDetailRes> getPrepayOrderById(@Parameter(description = "订单ID") @PathVariable Long id) {
        return ApiResponse.success(prepayOrderService.getPrepayOrderById(id));
    }

    /**
     * 分页查询预支付订单。
     *
     * @param req 分页和搜索条件
     * @return 预支付订单分页结果
     */
    @GetMapping
    @Operation(description = "分页查询预支付订单")
    public ApiResponse<PageResult<PrepayOrderPageRes>> pagePrepayOrder(@Valid @ModelAttribute PrepayOrderPageReq req) {
        return ApiResponse.success(prepayOrderService.pagePrepayOrder(req));
    }
}
