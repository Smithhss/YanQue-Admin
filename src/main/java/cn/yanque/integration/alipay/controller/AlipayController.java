package cn.yanque.integration.alipay.controller;

import cn.yanque.common.enums.OrderStatusEnum;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.AlipayProperties;
import cn.yanque.integration.alipay.service.impl.AlipayCashierServiceImpl;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import cn.yanque.models.order.prepay.pojo.info.UpdateOrderStatusInfo;
import cn.yanque.models.order.prepay.service.OrderService;
import cn.yanque.models.order.prepay.service.PrepayOrderService;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alipay")
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "alipay", matchIfMissing = true)
@Slf4j
public class AlipayController {

    @Autowired
    private AlipayCashierServiceImpl alipayCashierService;

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrepayOrderService prepayOrderService;

    @GetMapping("/cashier")
    public ResponseEntity<String> cashier(@RequestParam String orderNo) {
        OrderEntity order = orderService.selectByOrderNo(orderNo);
        if (order == null) {
            throw BusinessException.DateError.newInstance("支付订单不存在");
        }
        if (!OrderStatusEnum.PROCESSING.name().equals(order.getStatus())) {
            throw BusinessException.DateError.newInstance("支付订单状态不允许发起支付");
        }
        String html = alipayCashierService.buildPagePayForm(order);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store, no-cache, must-revalidate")
                .header("Pragma", "no-cache")
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @PostMapping("/notify")
    @ResponseBody
    public String notify(HttpServletRequest request) {
        Map<String, String> params = extractParams(request);
        if (!alipayCashierService.verifyNotify(params)) {
            return "failure";
        }

        String orderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        if (!"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)) {
            return "success";
        }

        OrderEntity order = orderService.selectByOrderNo(orderNo);
        if (order == null) {
            return "failure";
        }
        if (OrderStatusEnum.SUCCESS.name().equals(order.getStatus())) {
            return "success";
        }
        if (!OrderStatusEnum.PROCESSING.name().equals(order.getStatus())) {
            return "failure";
        }

        try {
            orderService.updateOrderStatus(new UpdateOrderStatusInfo(
                    orderNo,
                    OrderStatusEnum.SUCCESS.name(),
                    OrderStatusEnum.PROCESSING.name(),
                    params.get("trade_no"),
                    new Date()));
        } catch (BusinessException e) {
            log.warn("notify CAS update failed (concurrent?), orderNo={}, msg={}", orderNo, e.getMessage());
        }
        try {
            prepayOrderService.updatePrepayOrderSuccess(order.getPrepayOrderNo());
        } catch (BusinessException e) {
            log.warn("notify prepay update failed (concurrent?), prepayOrderNo={}, msg={}", order.getPrepayOrderNo(), e.getMessage());
        }
        return "success";
    }

    @GetMapping("/return")
    public RedirectView returnUrl(HttpServletRequest request) {
        Map<String, String> params = extractParams(request);

        // 验签：防止回调参数被篡改
        if (!alipayCashierService.verifyNotify(params)) {
            log.warn("alipay return signature verify failed, params={}", params);
            return new RedirectView(alipayProperties.getStudentWebReturnUrl() + "?error=sign_failed");
        }

        String orderNo = params.getOrDefault("out_trade_no", "");
        String redirectBase = alipayProperties.getStudentWebReturnUrl();

        // 同步回调仅做跳转，前端通过 paymentReturnInfo API 确认真实支付结果
        if (orderNo != null && !orderNo.isBlank()) {
            return new RedirectView(redirectBase + "?orderNo=" + orderNo);
        }
        return new RedirectView(redirectBase);
    }

    private Map<String, String> extractParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });
        return params;
    }
}
