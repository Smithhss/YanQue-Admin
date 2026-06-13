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

@Slf4j
public class YeepayCallbackServlet extends HttpServlet {

    public static final String CALLBACK_PREFIX = "/yop-callback";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            YopCallbackRequest callbackRequest = resolve(req);
            YopCallbackResponse callbackResponse = YopCallbackEngine.handle(callbackRequest);

            writeResponse(resp, callbackResponse);
        } catch (Exception e) {
            log.error("error when handle yop callback", e);
            resp.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }
    }

    private void writeResponse(HttpServletResponse resp, YopCallbackResponse callbackResponse) throws IOException {
        if (callbackResponse == null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (MapUtils.isNotEmpty(callbackResponse.getHeaders())) {
            callbackResponse.getHeaders().forEach(resp::addHeader);
        }
        if (callbackResponse.getContentType() != null) {
            resp.setContentType(callbackResponse.getContentType().getValue());
        }
        String body = StrUtil.nullToEmpty(callbackResponse.getBody());
        resp.getOutputStream().write(body.getBytes(YopConstants.DEFAULT_ENCODING));
    }

    private YopCallbackRequest resolve(HttpServletRequest req) throws IOException {
        final String contentTypeStr = req.getContentType();
        Object content = null;
        YopContentType contentType;
        if (StringUtils.startsWith(contentTypeStr, YOP_HTTP_CONTENT_TYPE_JSON)) {
            contentType = YopContentType.JSON;
            content = IOUtils.toString(req.getInputStream(), YopConstants.DEFAULT_ENCODING);
        } else {
            contentType = YopContentType.FORM_URL_ENCODE;
        }
        return new YopCallbackRequest(getCallbackType(req), req.getMethod())
                .setContentType(contentType)
                .setHeaders(getHeaders(req)).setParams(getParams(req)).setContent(content);
    }

    private String getCallbackType(HttpServletRequest req) {
        // 每个回调地址，对应一种通知类型
        return req.getRequestURI();
    }

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

    private Map<String, List<String>> getParams(HttpServletRequest request) {
        Map<String, List<String>> result = Maps.newHashMap();
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            result.put(param.getKey(), Arrays.asList(param.getValue()));
        }
        return result;
    }
}
