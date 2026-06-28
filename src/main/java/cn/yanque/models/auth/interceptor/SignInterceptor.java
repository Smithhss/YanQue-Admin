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

@Component
public class SignInterceptor implements HandlerInterceptor {

    private static final String HEADER_TIMESTAMP = "X-Timestamp";
    private static final String HEADER_NONCE = "X-Nonce";
    private static final String HEADER_SIGN = "X-Sign";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SIGN_SECRET_KEY_PREFIX = "yanque:sign:secret:";
    private static final String SIGN_NONCE_KEY_PREFIX = "yanque:sign:nonce:";
    private static final long ALLOWED_SKEW_MILLIS = 1000 * 60 * 60;
    private static final Duration NONCE_EXPIRE = Duration.ofMillis(ALLOWED_SKEW_MILLIS);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // JwtAuthInterceptor 已经把登录用户ID放到 request 中,这里按用户维度取签名密钥。
        Long userId = (Long) request.getAttribute("userId");
        String timestamp = request.getHeader(HEADER_TIMESTAMP);
        String nonce = request.getHeader(HEADER_NONCE);
        String sign = request.getHeader(HEADER_SIGN);

        if (userId == null) {
            writeUnauthorized(response, "用户未登录");
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

        long now = System.currentTimeMillis();
        // 时间戳只允许在一个较短窗口内有效,避免旧请求被长期保存后再次发送。
        if (Math.abs(now - requestTimestamp) > ALLOWED_SKEW_MILLIS) {
            writeUnauthorized(response, "请求超时");
            return false;
        }

        // 登录时下发并写入 Redis 的用户签名密钥,退出或过期后密钥失效。
        String secret = redisUtil.get(SIGN_SECRET_KEY_PREFIX + userId);
        if (isBlank(secret)) {
            writeUnauthorized(response, "签名密钥已失效,请重新登录");
            return false;
        }

        // nonce 只允许使用一次,setIfAbsent 成功说明这是第一次请求,失败说明可能是重复提交或重放攻击。
        String nonceKey = SIGN_NONCE_KEY_PREFIX + userId + ":" + nonce;
        Boolean nonceSaved = redisUtil.setIfAbsent(nonceKey, "1", NONCE_EXPIRE);
        if (!Boolean.TRUE.equals(nonceSaved)) {
            writeUnauthorized(response, "重复请求");
            return false;
        }

        // 前后端必须使用完全一致的签名原文,否则 HMAC 结果会不同。
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
        // 签名原文由请求方法,URI,查询参数,时间戳,nonce 组成,每段用换行隔开。
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
