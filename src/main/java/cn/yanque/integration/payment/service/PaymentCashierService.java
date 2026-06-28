package cn.yanque.integration.payment.service;

import cn.yanque.integration.payment.pojo.req.PaymentRefundReq;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentRefundRes;
import cn.yanque.integration.payment.pojo.res.PaymentTradeQueryRes;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;

public interface PaymentCashierService {

    PaymentUnifiedOrderRes unifiedOrder(PaymentUnifiedOrderReq req);

    PaymentRefundRes refund(PaymentRefundReq req);

    PaymentTradeQueryRes queryTrade(String orderNo);

    void closeTrade(String orderNo);
}
