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

/**
 * 学生前台正式登录JWT拦截器。
 */
@Component
public class StudentJwtAuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String STUDENT_ID = "uid";
    private static final String STUDENT_PHONE = "phone";
    private static final String STUDENT_FLAG = "student";
    private static final String EXPIRE_TIME = "expire_time";

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new BusinessException(401, "学生未登录或Token缺失");
        }

        String token = authorization.substring(BEARER_PREFIX.length()).trim();
        try {
            JWT jwt = JWT.of(token).setKey(sysConfigService.get(SysConfig.jwtSecret).getBytes());
            if (!jwt.verify()) {
                throw new BusinessException(401, "学生Token无效或已过期");
            }

            Object studentId = jwt.getPayload(STUDENT_ID);
            Object studentPhone = jwt.getPayload(STUDENT_PHONE);
            Object studentFlag = jwt.getPayload(STUDENT_FLAG);
            Object expireTime = jwt.getPayload(EXPIRE_TIME);
            if (studentId == null || studentPhone == null || expireTime == null || !Boolean.parseBoolean(String.valueOf(studentFlag))) {
                throw new BusinessException(401, "学生Token无效或已过期");
            }

            long expireTimestamp = Long.parseLong(String.valueOf(expireTime));
            if (System.currentTimeMillis() > expireTimestamp) {
                throw new BusinessException(401, "学生Token无效或已过期");
            }

            request.setAttribute("studentId", Long.parseLong(String.valueOf(studentId)));
            request.setAttribute("studentPhone", String.valueOf(studentPhone));
            return true;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(401, "学生Token无效或已过期");
        }
    }
}
