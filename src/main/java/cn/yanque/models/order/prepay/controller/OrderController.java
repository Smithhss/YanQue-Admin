package cn.yanque.models.order.prepay.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.api.PageResult;
import cn.yanque.models.order.prepay.pojo.vo.req.OrderPageReq;
import cn.yanque.models.order.prepay.pojo.vo.res.OrderPageRes;
import cn.yanque.models.order.prepay.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付订单管理接口。
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController", description = "支付订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询支付订单。
     *
     * @param req 分页和搜索条件
     * @return 支付订单分页结果
     */
    @GetMapping
    @Operation(description = "分页查询支付订单")
    public ApiResponse<PageResult<OrderPageRes>> pageOrder(@Valid @ModelAttribute OrderPageReq req) {
        return ApiResponse.success(orderService.pageOrder(req));
    }
}
