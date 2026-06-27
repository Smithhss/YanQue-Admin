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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "alipay", matchIfMissing = true)
public class AlipayCashierServiceImpl implements PaymentCashierService {

    private static final String PAGE_PAY_METHOD = "alipay.trade.page.pay";
    private static final String REFUND_METHOD = "alipay.trade.refund";
    private static final String VERSION = "1.0";
    private static final DateTimeFormatter ALIPAY_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        Map<String, String> params = buildCommonParams(REFUND_METHOD);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", req.getOrderNo());
        bizContent.put("refund_amount", money(req.getRefundAmount()));
        bizContent.put("refund_reason", req.getReason());
        bizContent.put("out_request_no", req.getRefundOrderNo());
        params.put("biz_content", bizContent.toJSONString());
        sign(params);

        try {
            String body = HttpClient.newHttpClient()
                    .send(HttpRequest.newBuilder(URI.create(alipayProperties.getGatewayUrl()))
                            .header("Content-Type", "application/x-www-form-urlencoded;charset=" + alipayProperties.getCharset())
                            .POST(HttpRequest.BodyPublishers.ofString(toFormBody(params), charset()))
                            .build(), HttpResponse.BodyHandlers.ofString(charset()))
                    .body();
            JSONObject response = JSONObject.parseObject(body).getJSONObject("alipay_trade_refund_response");
            if (response == null || !"10000".equals(response.getString("code"))) {
                String message = response == null ? "empty response" : response.getString("sub_msg");
                throw BusinessException.RemoteError.newInstance("支付宝退款失败: " + message);
            }
            PaymentRefundRes res = new PaymentRefundRes();
            res.setCode(response.getString("code"));
            res.setMessage(response.getString("msg"));
            res.setRefundRequestId(req.getRefundOrderNo());
            res.setUniqueRefundNo(response.getString("trade_no"));
            res.setStatus("SUCCESS");
            return res;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("alipay refund failed, orderNo={}", req.getOrderNo(), e);
            throw BusinessException.RemoteError.newInstance("支付宝退款异常: " + e.getMessage());
        }
    }

    public String buildPagePayForm(OrderEntity order) {
        assertConfigured();
        Map<String, String> params = buildCommonParams(PAGE_PAY_METHOD);
        params.put("notify_url", alipayProperties.getNotifyUrl());
        params.put("return_url", appendOrderNo(alipayProperties.getReturnUrl(), order.getOrderNo()));

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOrderNo());
        bizContent.put("total_amount", money(order.getOrderAmount()));
        bizContent.put("subject", getGoodsName());
        bizContent.put("product_code", alipayProperties.getProductCode());
        params.put("biz_content", bizContent.toJSONString());
        sign(params);

        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset=\"UTF-8\"><title>Alipay</title></head><body>");
        html.append("<form id=\"alipaySubmit\" name=\"alipaySubmit\" action=\"")
                .append(escapeHtml(alipayProperties.getGatewayUrl()))
                .append("\" method=\"post\">");
        params.forEach((key, value) -> html.append("<input type=\"hidden\" name=\"")
                .append(escapeHtml(key))
                .append("\" value=\"")
                .append(escapeHtml(value))
                .append("\"/>"));
        html.append("</form><script>document.forms['alipaySubmit'].submit();</script></body></html>");
        return html.toString();
    }

    public boolean verifyNotify(Map<String, String> params) {
        assertConfigured();
        String sign = params.get("sign");
        if (!StringUtils.hasText(sign)) {
            return false;
        }
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(getPublicKey(alipayProperties.getAlipayPublicKey()));
            signature.update(buildSignContent(params).getBytes(charset()));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            log.warn("alipay notify signature verify failed", e);
            return false;
        }
    }

    private Map<String, String> buildCommonParams(String method) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("app_id", alipayProperties.getAppId());
        params.put("method", method);
        params.put("format", alipayProperties.getFormat());
        params.put("charset", alipayProperties.getCharset());
        params.put("sign_type", alipayProperties.getSignType());
        params.put("timestamp", LocalDateTime.now().format(ALIPAY_TIME_FORMAT));
        params.put("version", VERSION);
        return params;
    }

    private void sign(Map<String, String> params) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(getPrivateKey(alipayProperties.getAppPrivateKey()));
            signature.update(buildSignContent(params).getBytes(charset()));
            params.put("sign", Base64.getEncoder().encodeToString(signature.sign()));
        } catch (GeneralSecurityException e) {
            throw BusinessException.RemoteError.newInstance("支付宝签名失败: " + e.getMessage());
        }
    }

    private String buildSignContent(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(entry -> !"sign".equals(entry.getKey()) && !"sign_type".equals(entry.getKey()))
                .filter(entry -> StringUtils.hasText(entry.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private PrivateKey getPrivateKey(String value) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(stripPem(value));
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private PublicKey getPublicKey(String value) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(stripPem(value));
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    private String stripPem(String value) {
        return value
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
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

    private String money(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }

    private String toFormBody(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, charset());
    }

    private Charset charset() {
        return StringUtils.hasText(alipayProperties.getCharset())
                ? Charset.forName(alipayProperties.getCharset())
                : StandardCharsets.UTF_8;
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
