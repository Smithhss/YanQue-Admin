package cn.yanque.integration.yeepay.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.config.YeepayProperties;
import cn.yanque.integration.payment.pojo.req.PaymentRefundReq;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentRefundRes;
import cn.yanque.integration.payment.pojo.res.PaymentTradeQueryRes;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;
import cn.yanque.integration.payment.service.PaymentCashierService;
import cn.yanque.integration.yeepay.pojo.req.YeepayRefundReq;
import cn.yanque.integration.yeepay.pojo.req.YeepayUnifiedOrderReq;
import cn.yanque.integration.yeepay.pojo.res.YeepayRefundRes;
import cn.yanque.integration.yeepay.pojo.res.YeepayUnifiedOrderRes;
import cn.yanque.integration.yeepay.service.YeepayCashierService;
import cn.yanque.integration.yeepay.service.YeepayGatewayService;
import com.alibaba.fastjson2.JSONObject;
import com.yeepay.yop.sdk.service.common.request.YopRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "yeepay")
public class YeepayCashierServiceImpl implements YeepayCashierService, PaymentCashierService {

    @Autowired
    private YeepayGatewayService yeepayGatewayService;

    @Autowired
    private YeepayProperties yeepayProperties;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public PaymentUnifiedOrderRes unifiedOrder(PaymentUnifiedOrderReq req) {
        YeepayUnifiedOrderReq yeepayReq = new YeepayUnifiedOrderReq();
        yeepayReq.setOrderNo(req.getOrderNo());
        yeepayReq.setOrderAmount(req.getOrderAmount());
        YeepayUnifiedOrderRes yeepayRes = unifiedOrder(yeepayReq);

        PaymentUnifiedOrderRes res = new PaymentUnifiedOrderRes();
        res.setUniqueOrderNo(yeepayRes.getUniqueOrderNo());
        res.setCashierUrl(yeepayRes.getCashierUrl());
        return res;
    }

    @Override
    public PaymentRefundRes refund(PaymentRefundReq req) {
        YeepayRefundReq yeepayReq = new YeepayRefundReq();
        yeepayReq.setOrderNo(req.getOrderNo());
        yeepayReq.setUniqueOrderNo(req.getUniqueOrderNo());
        yeepayReq.setRefundOrderNo(req.getRefundOrderNo());
        yeepayReq.setRefundAmount(req.getRefundAmount());
        yeepayReq.setReason(req.getReason());
        YeepayRefundRes yeepayRes = refund(yeepayReq);

        PaymentRefundRes res = new PaymentRefundRes();
        res.setCode(yeepayRes.getCode());
        res.setMessage(yeepayRes.getMessage());
        res.setUniqueRefundNo(yeepayRes.getUniqueRefundNo());
        res.setRefundRequestId(yeepayRes.getRefundRequestId());
        res.setStatus(yeepayRes.getStatus());
        return res;
    }

    @Override
    public YeepayUnifiedOrderRes unifiedOrder(YeepayUnifiedOrderReq req) {
        if (isMockMode()) {
            YeepayUnifiedOrderRes res = new YeepayUnifiedOrderRes();
            res.setUniqueOrderNo("MOCK-" + req.getOrderNo());
            String baseUrl = StringUtils.hasText(yeepayProperties.getMockCashierUrl())
                    ? yeepayProperties.getMockCashierUrl()
                    : yeepayProperties.getPaySuccessReturnUrl();
            res.setCashierUrl(baseUrl + "?orderNo=" + req.getOrderNo() + "&mockPay=true");
            return res;
        }

        YopRequest request = new YopRequest("/rest/v1.0/cashier/unified/order", "POST");
        request.addParameter("parentMerchantNo", yeepayProperties.getParentMerchantNo());
        request.addParameter("merchantNo", yeepayProperties.getMerchantNo());
        request.addParameter("orderId", req.getOrderNo());
        request.addParameter("orderAmount", req.getOrderAmount());
        request.addParameter("goodsName", sysConfigService.get(SysConfig.createOrderGoodsName));
        request.addParameter("notifyUrl", yeepayProperties.getPaySuccessNotifyUrl());
        request.addParameter("expiredTime", DateUtil.format(DateUtil.offsetMinute(new Date(), sysConfigService.get(SysConfig.createOrderExpireTime)), DatePattern.NORM_DATETIME_PATTERN));
        request.addParameter("returnUrl", yeepayProperties.getPaySuccessReturnUrl() + "?orderNo=" + req.getOrderNo());

        JSONObject result = yeepayGatewayService.request(request, "cashier/order");
        return JSONObject.parseObject(result.toJSONString(), YeepayUnifiedOrderRes.class);
    }

    @Override
    public YeepayRefundRes refund(YeepayRefundReq req) {
        if (isMockMode()) {
            YeepayRefundRes res = new YeepayRefundRes();
            res.setCode("OPR00000");
            res.setMessage("mock refund success");
            res.setUniqueRefundNo("MOCK-" + req.getRefundOrderNo());
            res.setRefundRequestId(req.getRefundOrderNo());
            res.setStatus("SUCCESS");
            return res;
        }

        YopRequest request = new YopRequest("/rest/v1.0/trade/refund", "POST");
        request.addParameter("parentMerchantNo", yeepayProperties.getParentMerchantNo());
        request.addParameter("merchantNo", yeepayProperties.getMerchantNo());
        request.addParameter("orderId", req.getOrderNo());
        request.addParameter("uniqueOrderNo", req.getUniqueOrderNo());
        request.addParameter("refundRequestId", req.getRefundOrderNo());
        request.addParameter("refundAmount", req.getRefundAmount());
        request.addParameter("description", req.getReason());
        request.addParameter("notifyUrl", yeepayProperties.getRefundNotifyUrl());

        JSONObject result = yeepayGatewayService.request(request, "refund");
        return JSONObject.parseObject(result.toJSONString(), YeepayRefundRes.class);
    }

    private boolean isMockMode() {
        return !StringUtils.hasText(yeepayProperties.getMode())
                || "mock".equalsIgnoreCase(yeepayProperties.getMode());
    }

    @Override
    public PaymentTradeQueryRes queryTrade(String orderNo) {
        PaymentTradeQueryRes res = new PaymentTradeQueryRes();
        res.setOutTradeNo(orderNo);
        if (isMockMode()) {
            res.setTradeStatus("NOT_FOUND");
            return res;
        }
        res.setTradeStatus("NOT_FOUND");
        return res;
    }

    @Override
    public void closeTrade(String orderNo) {
        // yeepay: no-op, mock mode or not implemented
    }
}
