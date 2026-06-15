package cn.yanque.models.order.refund.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.refund.biz.RefundOrderBiz;
import cn.yanque.models.order.refund.pojo.vo.req.RefundApplyReq;
import cn.yanque.models.order.refund.pojo.vo.req.RefundOrderPageReq;
import cn.yanque.models.order.refund.pojo.vo.res.RefundApplyRes;
import cn.yanque.models.order.refund.pojo.vo.res.RefundCreateRes;
import cn.yanque.models.order.refund.pojo.vo.res.RefundOrderPageRes;
import cn.yanque.models.order.refund.service.RefundOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "RefundOrderController", description = "退款订单管理")
public class RefundOrderController {

    @Autowired
    private RefundOrderBiz refundOrderBiz;

    @Autowired
    private RefundOrderService refundOrderService;

    @GetMapping("/refundOrders")
    @Operation(description = "分页查询退款订单")
    public ApiResponse<PageResult<RefundOrderPageRes>> pageRefundOrder(@Valid @ModelAttribute RefundOrderPageReq req) {
        return ApiResponse.success(refundOrderService.pageRefundOrder(req));
    }

    @PostMapping("/refundOrders/create")
    @Operation(description = "创建退款订单号")
    public ApiResponse<RefundCreateRes> createRefundOrder() {
        return ApiResponse.success(refundOrderBiz.createRefundOrder());
    }

    @PostMapping("/refundOrders/{refundOrderNo}/apply")
    @Operation(description = "申请退款")
    public ApiResponse<RefundApplyRes> applyRefund(@PathVariable String refundOrderNo, @Valid @RequestBody RefundApplyReq req) {
        return ApiResponse.success(refundOrderBiz.applyRefund(refundOrderNo, req));
    }
}
