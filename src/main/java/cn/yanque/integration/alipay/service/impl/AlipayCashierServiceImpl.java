package cn.yanque.integration.alipay.service.impl;

import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.AlipayProperties;
import cn.yanque.integration.payment.pojo.req.PaymentRefundReq;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentRefundRes;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;
import cn.yanque.integration.payment.service.PaymentCashierService;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "alipay", matchIfMissing = true)
public class AlipayCashierServiceImpl implements PaymentCashierService {

    private final AlipayProperties alipayProperties;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    public AlipayCashierServiceImpl(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
    }

    @Override
    public PaymentUnifiedOrderRes unifiedOrder(PaymentUnifiedOrderReq req) {
        PaymentUnifiedOrderRes res = new PaymentUnifiedOrderRes();
        res.setUniqueOrderNo("ALIPAY-" + req.getOrderNo());
        res.setCashierUrl(UriComponentsBuilder.fromUriString(alipayProperties.getCashierUrl())
                .queryParam("orderNo", req.getOrderNo())
                .build()
                .toUriString());
        return res;
    }

    @Override
    public PaymentRefundRes refund(PaymentRefundReq req) {
        assertConfigured();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", req.getOrderNo());
        bizContent.put("refund_amount", req.getRefundAmount());
        bizContent.put("refund_reason", req.getReason());
        bizContent.put("out_request_no", req.getRefundOrderNo());
        request.setBizContent(bizContent.toJSONString());

        try {
            AlipayTradeRefundResponse response = buildClient().execute(request);
            if (!response.isSuccess()) {
                throw BusinessException.RemoteError.newInstance("支付宝退款失败: " + response.getSubMsg());
            }
            PaymentRefundRes res = new PaymentRefundRes();
            res.setCode(response.getCode());
            res.setMessage(response.getMsg());
            res.setRefundRequestId(req.getRefundOrderNo());
            res.setUniqueRefundNo(response.getTradeNo());
            res.setStatus("SUCCESS");
            return res;
        } catch (AlipayApiException e) {
            log.error("alipay refund failed, orderNo={}", req.getOrderNo(), e);
            throw BusinessException.RemoteError.newInstance("支付宝退款异常: " + e.getMessage());
        }
    }

    public String buildPagePayForm(OrderEntity order) {
        assertConfigured();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayProperties.getNotifyUrl());
        request.setReturnUrl(appendOrderNo(alipayProperties.getReturnUrl(), order.getOrderNo()));

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("total_amount", order.getOrderAmount());
        bizContent.put("subject", getGoodsName());
        bizContent.put("product_code", alipayProperties.getProductCode());
        request.setBizContent(bizContent.toJSONString());

        try {
            AlipayTradePagePayResponse response = buildClient().pageExecute(request);
            if (!StringUtils.hasText(response.getBody())) {
                throw BusinessException.RemoteError.newInstance("支付宝页面支付创建失败");
            }
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("alipay page pay failed, orderNo={}", order.getOrderNo(), e);
            throw BusinessException.RemoteError.newInstance("支付宝页面支付异常: " + e.getMessage());
        }
    }

    private AlipayClient buildClient() {
        return new DefaultAlipayClient(
                alipayProperties.getGatewayUrl(),
                alipayProperties.getAppId(),
                alipayProperties.getAppPrivateKey(),
                alipayProperties.getFormat(),
                alipayProperties.getCharset(),
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getSignType());
    }

    private String getGoodsName() {
        if (sysConfigService == null) {
            return "YanQue Education Order";
        }
        return sysConfigService.get(SysConfig.createOrderGoodsName);
    }

    private void assertConfigured() {
        if (!StringUtils.hasText(alipayProperties.getGatewayUrl())
                || !StringUtils.hasText(alipayProperties.getAppId())
                || !StringUtils.hasText(alipayProperties.getAppPrivateKey())
                || !StringUtils.hasText(alipayProperties.getAlipayPublicKey())) {
            throw BusinessException.RemoteError.newInstance("支付宝沙箱配置未完整");
        }
    }

    private String appendOrderNo(String url, String orderNo) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("orderNo", orderNo)
                .build()
                .toUriString();
    }
}
