package cn.yanque.integration.yeepay.callback;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.yeepay.yop.sdk.YopConstants;
import com.yeepay.yop.sdk.http.YopContentType;
import com.yeepay.yop.sdk.service.common.YopCallbackEngine;
import com.yeepay.yop.sdk.service.common.callback.YopCallbackRequest;
import com.yeepay.yop.sdk.service.common.callback.YopCallbackResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static com.yeepay.yop.sdk.YopConstants.YOP_HTTP_CONTENT_TYPE_JSON;

/**
 * 易宝支付回调入口 Servlet。
 *
 * 设计模式:Servlet(适配器) → YopCallbackEngine(路由器) → Handler(业务处理)
 *
 * 为什么用 Servlet 而非 Controller？
 * - 易宝 SDK 的 YopCallbackEngine.handle() 需要原始 HTTP 请求信息
 * - Controller 的 @RequestBody 会提前消费 InputStream,SDK 拿不到原始数据
 *
 * 回调类型(按 URI 路由):
 * - /yq-admin/yop-callback/paySuccess  → YeepayPaySuccessHandle(支付成功)
 * - /yq-admin/yop-callback/refund      → YeepayRefundHandle(退款结果)
 */
@Slf4j
public class YeepayCallbackServlet extends HttpServlet {

    // 回调路径前缀,YeepayCallbackConfig 注册 Servlet 时使用
    public static final String CALLBACK_PREFIX = "/yop-callback";

    // 防御性编程:统一走 doPost,防止易宝用 GET 发回调
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    /**
     * 回调主流程:解析请求 → 路由到 Handler → 返回响应。
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. 把 HttpServletRequest 转为 SDK 能理解的 YopCallbackRequest
            YopCallbackRequest callbackRequest = resolve(req);
            // 2. SDK 根据 URI 路由到对应的 Handler(如 YeepayPaySuccessHandle)
            YopCallbackResponse callbackResponse = YopCallbackEngine.handle(callbackRequest);
            // 3. 把 SDK 的响应写回给易宝
            writeResponse(resp, callbackResponse);
        } catch (Exception e) {
            log.error("error when handle yop callback", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 把 SDK 的响应写回给易宝服务器。
     * - 返回 200 OK → 易宝不再重发回调
     * - 返回 500 → 易宝会重试(通常最多 3 次)
     */
    private void writeResponse(HttpServletResponse resp, YopCallbackResponse callbackResponse) throws IOException {
        if (callbackResponse == null) {
            // 某些回调不需要返回内容,直接返回 200 避免易宝重发
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // 写回 SDK 生成的响应头(如签名等)
        if (MapUtils.isNotEmpty(callbackResponse.getHeaders())) {
            callbackResponse.getHeaders().forEach(resp::addHeader);
        }
        if (callbackResponse.getContentType() != null) {
            resp.setContentType(callbackResponse.getContentType().getValue());
        }
        // 写回响应体
        String body = StrUtil.nullToEmpty(callbackResponse.getBody());
        resp.getOutputStream().write(body.getBytes(YopConstants.DEFAULT_ENCODING));
    }

    /**
     * 把 HttpServletRequest 解析为 YopCallbackRequest。
     *
     * 解析内容:
     * - callbackType: URI 路径,用于路由到对应 Handler
     * - headers: 请求头,用于 SDK 验签
     * - params: 请求参数
     * - content: 请求体(JSON 或 Form 表单)
     */
    private YopCallbackRequest resolve(HttpServletRequest req) throws IOException {
        final String contentTypeStr = req.getContentType();
        Object content = null;
        YopContentType contentType;

        // 判断 Content-Type:JSON 需手动读 InputStream,Form 由容器自动解析
        if (StringUtils.startsWith(contentTypeStr, YOP_HTTP_CONTENT_TYPE_JSON)) {
            contentType = YopContentType.JSON;
            content = IOUtils.toString(req.getInputStream(), YopConstants.DEFAULT_ENCODING);
        } else {
            contentType = YopContentType.FORM_URL_ENCODE;
        }

        // 组装成 SDK 需要的请求对象
        return new YopCallbackRequest(getCallbackType(req), req.getMethod())
                .setContentType(contentType)
                .setHeaders(getHeaders(req))
                .setParams(getParams(req))
                .setContent(content);
    }

    /**
     * 获取回调类型(URI 路径),作为路由依据。
     * 例如:/yq-admin/yop-callback/paySuccess
     * YopCallbackEngine 根据此 URI 查找已注册的 Handler。
     */
    private String getCallbackType(HttpServletRequest req) {
        return req.getRequestURI();
    }

    /**
     * 提取所有请求头,转为 Map<String, String>。
     * SDK 需要用这些头信息进行签名验证。
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> result = Maps.newHashMap();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final String headerValue = request.getHeader(headerName);
            result.put(headerName, headerValue);
        }
        return result;
    }

    /**
     * 提取所有请求参数,转为 Map<String, List<String>>。
     * Servlet 的 getParameterMap() 返回 String[],SDK 需要 List<String>。
     */
    private Map<String, List<String>> getParams(HttpServletRequest request) {
        Map<String, List<String>> result = Maps.newHashMap();
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            result.put(param.getKey(), Arrays.asList(param.getValue()));
        }
        return result;
    }
}
