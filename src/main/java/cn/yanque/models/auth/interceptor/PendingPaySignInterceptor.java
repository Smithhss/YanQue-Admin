package cn.yanque.models.auth.interceptor;

import cn.hutool.jwt.JWT;
import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.utils.RedisUtil;
import cn.yanque.models.order.prepay.mapper.PrepayOrderMapper;
import cn.yanque.models.order.prepay.pojo.entity.PrepayOrderEntity;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.HexFormat;

@Component
public class PendingPaySignInterceptor implements HandlerInterceptor {

    private static final String HEADER_PENDING_PAY_TOKEN = "X-Pending-Pay-Token";
    private static final String HEADER_TIMESTAMP = "X-Timestamp";
    private static final String HEADER_NONCE = "X-Nonce";
    private static final String HEADER_SIGN = "X-Sign";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String PENDING_PAY_KEY_PREFIX = "yanque:student:pending-pay:";
    private static final String PENDING_PAY_NONCE_KEY_PREFIX = "yanque:student:pending-pay:nonce:";
    private static final String STATUS_PENDING_PAYMENT = "PENDING_PAYMENT";
    private static final String TOKEN_TYPE_PENDING_PAY = "PENDING_PAY";
    private static final long ALLOWED_SKEW_MILLIS = 1000 * 60 * 60;
    private static final Duration NONCE_EXPIRE = Duration.ofMillis(ALLOWED_SKEW_MILLIS);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PrepayOrderMapper prepayOrderMapper;

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_PENDING_PAY_TOKEN);
        String timestamp = request.getHeader(HEADER_TIMESTAMP);
        String nonce = request.getHeader(HEADER_NONCE);
        String sign = request.getHeader(HEADER_SIGN);

        if (isBlank(token) || isBlank(timestamp) || isBlank(nonce) || isBlank(sign)) {
            writeUnauthorized(response, "待支付签名参数缺失");
            return false;
        }

        PendingPayJwtInfo jwtInfo = parsePendingPayJwt(token);
        if (jwtInfo == null) {
            writeUnauthorized(response, "待支付Token无效或已过期");
            return false;
        }

        String pendingPayInfo = redisUtil.get(PENDING_PAY_KEY_PREFIX + token);
        if (isBlank(pendingPayInfo)) {
            writeUnauthorized(response, "待支付登录状态已失效，请重新登录");
            return false;
        }
        String[] parts = pendingPayInfo.split("\\|", 3);
        if (parts.length != 3 || isBlank(parts[0]) || isBlank(parts[1]) || isBlank(parts[2])) {
            writeUnauthorized(response, "待支付登录状态异常，请重新登录");
            return false;
        }
        String studentPhone = parts[0];
        String prepayOrderNo = parts[1];
        String signSecret = parts[2];
        if (!studentPhone.equals(jwtInfo.studentPhone()) || !prepayOrderNo.equals(jwtInfo.prepayOrderNo())) {
            writeUnauthorized(response, "待支付Token与登录状态不匹配");
            return false;
        }

        long requestTimestamp;
        try {
            requestTimestamp = Long.parseLong(timestamp);
        } catch (NumberFormatException ex) {
            writeUnauthorized(response, "时间戳格式错误");
            return false;
        }
        if (Math.abs(System.currentTimeMillis() - requestTimestamp) > ALLOWED_SKEW_MILLIS) {
            writeUnauthorized(response, "请求超时");
            return false;
        }

        String nonceKey = PENDING_PAY_NONCE_KEY_PREFIX + token + ":" + nonce;
        Boolean nonceSaved = redisUtil.setIfAbsent(nonceKey, "1", NONCE_EXPIRE);
        if (!Boolean.TRUE.equals(nonceSaved)) {
            writeUnauthorized(response, "重复请求");
            return false;
        }

        String source = buildSignSource(request, timestamp, nonce);
        String serverSign = hmacSha256Hex(source, signSecret);
        if (!equalsIgnoreCaseSecure(serverSign, sign)) {
            writeUnauthorized(response, "签名错误");
            return false;
        }

        PrepayOrderEntity prepayOrder = prepayOrderMapper.selectByOrderNo(prepayOrderNo);
        if (prepayOrder == null || !studentPhone.equals(prepayOrder.getStudentPhone())) {
            writeUnauthorized(response, "待支付订单状态异常");
            return false;
        }
        if (isPaymentCreatingRequest(request) && !STATUS_PENDING_PAYMENT.equals(prepayOrder.getOrderStatus())) {
            writeUnauthorized(response, "待支付订单状态异常");
            return false;
        }

        request.setAttribute("pendingPayStudentPhone", studentPhone);
        request.setAttribute("pendingPayPrepayOrderNo", prepayOrderNo);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private PendingPayJwtInfo parsePendingPayJwt(String token) {
        try {
            JWT jwt = JWT.of(token).setKey(sysConfigService.get(SysConfig.jwtSecret).getBytes());
            if (!jwt.verify()) {
                return null;
            }
            Object tokenType = jwt.getPayload("token_type");
            Object studentPhone = jwt.getPayload("phone");
            Object prepayOrderNo = jwt.getPayload("prepay_order_no");
            Object expireTime = jwt.getPayload("expire_time");
            if (!TOKEN_TYPE_PENDING_PAY.equals(String.valueOf(tokenType))
                    || studentPhone == null
                    || prepayOrderNo == null
                    || expireTime == null) {
                return null;
            }
            long expireTimestamp = Long.parseLong(String.valueOf(expireTime));
            if (System.currentTimeMillis() > expireTimestamp) {
                return null;
            }
            return new PendingPayJwtInfo(String.valueOf(studentPhone), String.valueOf(prepayOrderNo));
        } catch (Exception e) {
            return null;
        }
    }

    private String buildSignSource(HttpServletRequest request, String timestamp, String nonce) {
        String queryString = request.getQueryString() == null ? "" : request.getQueryString();
        return request.getMethod().toUpperCase()
                + "\n" + request.getRequestURI()
                + "\n" + queryString
                + "\n" + timestamp
                + "\n" + nonce;
    }

    private boolean isPaymentCreatingRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.endsWith("/createOrderNo") || requestURI.endsWith("/createPaymentOrder");
    }

    private String hmacSha256Hex(String source, String secret) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKeySpec);
        return HexFormat.of().formatHex(mac.doFinal(source.getBytes(StandardCharsets.UTF_8)));
    }

    private boolean equalsIgnoreCaseSecure(String expected, String actual) {
        return MessageDigest.isEqual(
                expected.toLowerCase().getBytes(StandardCharsets.UTF_8),
                actual.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse<Void> error = ApiResponse.fail(401, message);
        response.getWriter().write(JSONObject.toJSONString(error));
    }

    private record PendingPayJwtInfo(String studentPhone, String prepayOrderNo) {
    }
}
