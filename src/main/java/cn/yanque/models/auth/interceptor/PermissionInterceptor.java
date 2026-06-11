package cn.yanque.models.auth.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.yanque.common.annotations.NoAuthCheck;
import cn.yanque.common.api.ApiResponse;
import cn.yanque.common.enums.PermissionTypeEnum;
import cn.yanque.models.users.pojo.entity.SysPermissionEntity;
import cn.yanque.models.users.pojo.info.UserInfo;
import cn.yanque.models.users.service.SysUserService;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // JWT 校验通过后才能拿到 userId，这里基于当前用户查询角色和权限。
        Long userId = (Long) request.getAttribute("userId");

        UserInfo userInfo = sysUserService.getUserInfo(userId);

        List<SysPermissionEntity> sysPermissionEntityList = userInfo.getSysPermissionEntityList();

        // 超级管理员直接放行，方便后台初始化和教学演示。
        boolean superAdmin = userInfo.getSysRoleEntityList().stream().anyMatch(sysRoleEntity -> sysRoleEntity.getRoleCode().equals("SUPER_ADMIN"));
        if (superAdmin) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }


        HandlerMethod method = (HandlerMethod) handler;

        // 标了 NoAuthCheck 的接口跳过权限校验，适合少量公开接口。
        if (method.hasMethodAnnotation(NoAuthCheck.class) || method.getBeanType().isAnnotationPresent(NoAuthCheck.class)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        if (CollectionUtil.isEmpty(sysPermissionEntityList)) {
            writeUnauthorized(response, "没有权限");
            return false;
        }

        String requestURI = request.getRequestURI();
        // 只匹配 API 类型权限；路径使用 AntPathMatcher，所以 /api/course/{id} 可以匹配实际ID路径。
        boolean flag = sysPermissionEntityList.stream()
                .filter(sysPermissionEntity
                        -> sysPermissionEntity.getPermissionType().equals(PermissionTypeEnum.API.name()))
                .anyMatch(sysPermissionEntity -> PATH_MATCHER.match(sysPermissionEntity.getApiPath(), requestURI));

        if (!flag) {
            writeUnauthorized(response, "暂无接口权限");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiResponse<Void> error = ApiResponse.fail(403, message);
        response.getWriter().write(JSONObject.toJSONString(error));
    }
}
