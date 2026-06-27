package cn.yanque.integration.alipay.service.impl;

import cn.yanque.config.AlipayProperties;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
