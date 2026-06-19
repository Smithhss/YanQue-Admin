package cn.yanque.models.auth.interceptor;

import cn.hutool.jwt.JWT;
import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID = "uid";
    private static final String EXPIRE_TIME = "expire_time";

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 前端统一使用 Authorization: Bearer xxx 传 token。
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new BusinessException(401, "未登录或Token缺失");
        }

        String token = authorization.substring(BEARER_PREFIX.length()).trim();
        try {
            // JWT 密钥从系统配置读取，方便后续通过配置页面调整。
            JWT jwt = JWT.of(token).setKey(sysConfigService.get(SysConfig.jwtSecret).getBytes());
            if (!jwt.verify()) {
                throw new BusinessException(401, "Token无效或已过期");
            }

            Object userId = jwt.getPayload(USER_ID);
            Object expireTime = jwt.getPayload(EXPIRE_TIME);

            if (userId == null || expireTime == null) {
                throw new BusinessException(401, "Token无效或已过期");
            }

            long expireTimestamp = Long.parseLong(String.valueOf(expireTime));
            if (System.currentTimeMillis() > expireTimestamp) {
                throw new BusinessException(401, "Token无效或已过期");
            }

            // 后续 SignInterceptor 和 PermissionInterceptor 都从 request attribute 中获取当前用户ID。
            request.setAttribute("userId", Long.parseLong(String.valueOf(userId)));
            return true;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(401, "Token无效或已过期");
        }
    }
}
