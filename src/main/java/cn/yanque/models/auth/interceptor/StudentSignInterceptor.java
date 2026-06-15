package cn.yanque.models.auth.interceptor;

import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.utils.RedisUtil;
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

/**
 * 学生前台正式登录签名拦截器。
 */
@Component
public class StudentSignInterceptor implements HandlerInterceptor {

    private static final String HEADER_TIMESTAMP = "X-Timestamp";
    private static final String HEADER_NONCE = "X-Nonce";
    private static final String HEADER_SIGN = "X-Sign";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SIGN_SECRET_KEY_PREFIX = "yanque:student:sign:secret:";
    private static final String SIGN_NONCE_KEY_PREFIX = "yanque:student:sign:nonce:";
    private static final long ALLOWED_SKEW_MILLIS = 1000 * 60 * 60;
    private static final Duration NONCE_EXPIRE = Duration.ofMillis(ALLOWED_SKEW_MILLIS);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long studentId = (Long) request.getAttribute("studentId");
        String timestamp = request.getHeader(HEADER_TIMESTAMP);
        String nonce = request.getHeader(HEADER_NONCE);
        String sign = request.getHeader(HEADER_SIGN);

        if (studentId == null) {
            writeUnauthorized(response, "学生未登录");
            return false;
        }
        if (isBlank(timestamp) || isBlank(nonce) || isBlank(sign)) {
            writeUnauthorized(response, "签名参数缺失");
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

        String secret = redisUtil.get(SIGN_SECRET_KEY_PREFIX + studentId);
        if (isBlank(secret)) {
            writeUnauthorized(response, "签名密钥已失效，请重新登录");
            return false;
        }

        String nonceKey = SIGN_NONCE_KEY_PREFIX + studentId + ":" + nonce;
        Boolean nonceSaved = redisUtil.setIfAbsent(nonceKey, "1", NONCE_EXPIRE);
        if (!Boolean.TRUE.equals(nonceSaved)) {
            writeUnauthorized(response, "重复请求");
            return false;
        }

        String source = buildSignSource(request, timestamp, nonce);
        String serverSign = hmacSha256Hex(source, secret);
        if (!equalsIgnoreCaseSecure(serverSign, sign)) {
            writeUnauthorized(response, "签名错误");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private String buildSignSource(HttpServletRequest request, String timestamp, String nonce) {
        String queryString = request.getQueryString() == null ? "" : request.getQueryString();
        return request.getMethod().toUpperCase()
                + "\n" + request.getRequestURI()
                + "\n" + queryString
                + "\n" + timestamp
                + "\n" + nonce;
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
}
