package cn.yanque.studentFront.controller;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.studentFront.biz.StudentOrderBiz;
import cn.yanque.studentFront.pojo.req.CreatePaymentOrderReq;
import cn.yanque.studentFront.pojo.res.CreateOrderNoRes;
import cn.yanque.studentFront.pojo.res.CreatePaymentOrderRes;
import cn.yanque.studentFront.pojo.res.PaymentReturnInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/pending/order")
@Tag(name = "StudentFrontOrderController", description = "学生订单")
public class StudentFrontOrderController {

    @Autowired
    private StudentOrderBiz studentOrderBiz;

    @PostMapping("/createOrderNo")
    @Operation(description = "生成订单号")
    public ApiResponse<CreateOrderNoRes> createOrderNo() {
        return ApiResponse.success(studentOrderBiz.createOrderNo());
    }


    @PostMapping("/createPaymentOrder")
    @Operation(description = "学生支付下单")
    public ApiResponse<CreatePaymentOrderRes> createPaymentOrder(@Valid @RequestBody CreatePaymentOrderReq req) {
        return ApiResponse.success(studentOrderBiz.createPaymentOrder(req));
    }

    @GetMapping("/paymentReturnInfo")
    @Operation(description = "查询支付成功回跳页订单信息")
    public ApiResponse<PaymentReturnInfoRes> paymentReturnInfo(@RequestParam String orderNo) {
        return ApiResponse.success(studentOrderBiz.paymentReturnInfo(orderNo));
    }

}
