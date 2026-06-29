package cn.yanque.integration.alipay.service.impl;

import cn.yanque.config.AlipayProperties;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlipayCashierServiceImplTest {

    @Test
    void unifiedOrderReturnsLocalCashierUrl() {
        AlipayProperties properties = new AlipayProperties();
        properties.setCashierUrl("/yq-admin/alipay/cashier");

        AlipayCashierServiceImpl service = new AlipayCashierServiceImpl(properties);
        PaymentUnifiedOrderReq req = new PaymentUnifiedOrderReq();
        req.setOrderNo("101020260627000001");
        req.setOrderAmount(new BigDecimal("199.00"));

        PaymentUnifiedOrderRes res = service.unifiedOrder(req);

        assertEquals("ALIPAY-101020260627000001", res.getUniqueOrderNo());
        assertTrue(res.getCashierUrl().startsWith("/yq-admin/alipay/cashier?"));
        assertTrue(res.getCashierUrl().contains("orderNo=101020260627000001"));
    }

    @Test
    void buildPagePayUsesGetAndNotifyOnlyByDefault() {
        AlipayProperties properties = configuredAlipayProperties();
        properties.setNotifyUrl("http://notify.example.com/yq-admin/alipay/notify");
        properties.setReturnUrl("http://return.example.com/yq-admin/alipay/return");
        properties.setPagePayHttpMethod("GET");
        properties.setReturnUrlEnabled(false);

        CapturingAlipayCashierService service = new CapturingAlipayCashierService(properties);
        String cashier = service.buildPagePayForm(order("101020260629000001", "0.01"));

        assertEquals("https://alipay.example.com/cashier", cashier);
        assertEquals("GET", service.capturedHttpMethod);
        assertEquals("http://notify.example.com/yq-admin/alipay/notify", service.capturedRequest.getNotifyUrl());
        assertNull(service.capturedRequest.getReturnUrl());

        JSONObject bizContent = JSONObject.parseObject(service.capturedRequest.getBizContent());
        assertEquals("101020260629000001", bizContent.getString("out_trade_no"));
        assertEquals("0.01", bizContent.getString("total_amount"));
        assertEquals("FAST_INSTANT_TRADE_PAY", bizContent.getString("product_code"));
    }

    @Test
    void buildPagePayIncludesReturnUrlOnlyWhenEnabled() {
        AlipayProperties properties = configuredAlipayProperties();
        properties.setNotifyUrl("http://notify.example.com/yq-admin/alipay/notify");
        properties.setReturnUrl("http://return.example.com/yq-admin/alipay/return");
        properties.setReturnUrlEnabled(true);

        CapturingAlipayCashierService service = new CapturingAlipayCashierService(properties);
        service.buildPagePayForm(order("101020260629000002", "0.01"));

        assertEquals("http://return.example.com/yq-admin/alipay/return", service.capturedRequest.getReturnUrl());
    }

    private AlipayProperties configuredAlipayProperties() {
        AlipayProperties properties = new AlipayProperties();
        properties.setGatewayUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        properties.setAppId("sandbox-app-id");
        properties.setAppPrivateKey("sandbox-private-key");
        properties.setAlipayPublicKey("sandbox-public-key");
        properties.setProductCode("FAST_INSTANT_TRADE_PAY");
        return properties;
    }

    private OrderEntity order(String orderNo, String amount) {
        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNo);
        order.setOrderAmount(new BigDecimal(amount));
        return order;
    }

    private static class CapturingAlipayCashierService extends AlipayCashierServiceImpl {

        private AlipayTradePagePayRequest capturedRequest;
        private String capturedHttpMethod;

        private CapturingAlipayCashierService(AlipayProperties alipayProperties) {
            super(alipayProperties);
        }

        @Override
        protected AlipayTradePagePayResponse pageExecute(AlipayTradePagePayRequest request, String httpMethod) throws AlipayApiException {
            capturedRequest = request;
            capturedHttpMethod = httpMethod;
            AlipayTradePagePayResponse response = new AlipayTradePagePayResponse();
            response.setBody("https://alipay.example.com/cashier");
            return response;
        }
    }
}
