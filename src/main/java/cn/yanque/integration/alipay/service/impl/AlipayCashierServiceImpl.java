package cn.yanque.integration.alipay.service.impl;

import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.config.AlipayProperties;
import cn.yanque.integration.payment.pojo.req.PaymentRefundReq;
import cn.yanque.integration.payment.pojo.req.PaymentUnifiedOrderReq;
import cn.yanque.integration.payment.pojo.res.PaymentRefundRes;
import cn.yanque.integration.payment.pojo.res.PaymentTradeQueryRes;
import cn.yanque.integration.payment.pojo.res.PaymentUnifiedOrderRes;
import cn.yanque.integration.payment.service.PaymentCashierService;
import cn.yanque.models.order.prepay.pojo.entity.OrderEntity;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "alipay", matchIfMissing = true)
public class AlipayCashierServiceImpl implements PaymentCashierService {

    private final AlipayClient alipayClient;
    private final AlipayProperties alipayProperties;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    public AlipayCashierServiceImpl(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
        this.alipayClient = new DefaultAlipayClient(
                alipayProperties.getGatewayUrl(),
                alipayProperties.getAppId(),
                alipayProperties.getAppPrivateKey(),
                alipayProperties.getFormat(),
                alipayProperties.getCharset(),
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getSignType()
        );
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
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", req.getOrderNo());
            bizContent.put("refund_amount", money(req.getRefundAmount()));
            bizContent.put("refund_reason", req.getReason());
            bizContent.put("out_request_no", req.getRefundOrderNo());
            request.setBizContent(bizContent.toJSONString());

            AlipayTradeRefundResponse response = alipayClient.execute(request);
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
        try {
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            if (StringUtils.hasText(alipayProperties.getNotifyUrl())) {
                request.setNotifyUrl(alipayProperties.getNotifyUrl());
            }
            if (isReturnUrlEnabled() && StringUtils.hasText(alipayProperties.getReturnUrl())) {
                request.setReturnUrl(alipayProperties.getReturnUrl());
            }

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order.getOrderNo());
            bizContent.put("total_amount", money(order.getOrderAmount()));
            bizContent.put("subject", getGoodsName());
            bizContent.put("product_code", alipayProperties.getProductCode());
            request.setBizContent(bizContent.toJSONString());

            AlipayTradePagePayResponse response = pageExecute(request, pagePayHttpMethod());
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("alipay page pay failed, orderNo={}", order.getOrderNo(), e);
            throw BusinessException.RemoteError.newInstance("支付宝下单失败: " + e.getMessage());
        }
    }

    protected AlipayTradePagePayResponse pageExecute(AlipayTradePagePayRequest request, String httpMethod) throws AlipayApiException {
        return alipayClient.pageExecute(request, httpMethod);
    }

    public boolean verifyNotify(Map<String, String> params) {
        assertConfigured();
        try {
            return AlipaySignature.rsaCheckV1(
                    params,
                    alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getCharset(),
                    alipayProperties.getSignType()
            );
        } catch (AlipayApiException e) {
            log.warn("alipay notify signature verify failed", e);
            return false;
        }
    }

    @Override
    public PaymentTradeQueryRes queryTrade(String orderNo) {
        assertConfigured();
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toJSONString());

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            PaymentTradeQueryRes res = new PaymentTradeQueryRes();
            if (response.isSuccess()) {
                res.setTradeStatus(response.getTradeStatus());
                res.setTradeNo(response.getTradeNo());
                res.setOutTradeNo(response.getOutTradeNo());
            } else {
                res.setTradeStatus("NOT_FOUND");
            }
            return res;
        } catch (AlipayApiException e) {
            log.error("alipay trade query failed, orderNo={}", orderNo, e);
            PaymentTradeQueryRes res = new PaymentTradeQueryRes();
            res.setTradeStatus("QUERY_FAILED");
            return res;
        }
    }

    @Override
    public void closeTrade(String orderNo) {
        assertConfigured();
        try {
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toJSONString());

            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                log.warn("alipay trade close failed or already closed, orderNo={}, subMsg={}", orderNo, response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("alipay trade close exception, orderNo={}", orderNo, e);
        }
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

    private String pagePayHttpMethod() {
        if (!StringUtils.hasText(alipayProperties.getPagePayHttpMethod())) {
            return "GET";
        }
        return alipayProperties.getPagePayHttpMethod().trim().toUpperCase();
    }

    private boolean isReturnUrlEnabled() {
        return Boolean.TRUE.equals(alipayProperties.getReturnUrlEnabled());
    }

    private String money(BigDecimal amount) {
        if (amount == null) {
            throw BusinessException.DateError.newInstance("支付金额不能为空");
        }
        return amount.stripTrailingZeros().toPlainString();
    }
}
