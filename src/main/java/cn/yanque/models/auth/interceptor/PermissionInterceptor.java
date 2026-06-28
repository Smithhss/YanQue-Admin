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

/**
 * 接口权限校验拦截器。
 *
 * 执行顺序:JwtAuthInterceptor → SignInterceptor → PermissionInterceptor
 * 前置条件:JwtAuthInterceptor 已将 userId 写入 request attribute
 *
 * 放行逻辑(按优先级):
 * 1. 超级管理员(SUPER_ADMIN)直接放行
 * 2. 标注 @NoAuthCheck 的方法或类直接放行
 * 3. 权限列表中存在 API 类型权限且 AntPathMatcher 匹配当前路径则放行
 * 其余情况返回 403
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    // Spring 提供的路径匹配器,支持 Ant 风格通配符(如 /api/user/{id})
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 JwtAuthInterceptor 存入的 request attribute 中获取当前登录用户ID
        Long userId = (Long) request.getAttribute("userId");

        // 一次性查询用户完整信息:用户实体 + 角色列表 + 权限列表
        // UserInfo 是聚合对象,聚合了 sys_user,sys_role,sys_permission 三张表的数据
        UserInfo userInfo = sysUserService.getUserInfo(userId);
        List<SysPermissionEntity> sysPermissionEntityList = userInfo.getSysPermissionEntityList();

        // ===== 放行条件 1:超级管理员 =====
        // 遍历角色列表,检查是否存在 roleCode 为 SUPER_ADMIN 的角色
        // 超管不受权限限制,方便系统初始化和教学演示
        boolean superAdmin = userInfo.getSysRoleEntityList().stream()
                .anyMatch(sysRoleEntity -> sysRoleEntity.getRoleCode().equals("SUPER_ADMIN"));
        if (superAdmin) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 将 handler 强转为 HandlerMethod,才能获取方法上的注解信息
        HandlerMethod method = (HandlerMethod) handler;

        // ===== 放行条件 2:@NoAuthCheck 注解 =====
        // 支持两种标注方式:
        //   方法级:@NoAuthCheck 标注在 Controller 方法上,只跳过该方法
        //   类级:@NoAuthCheck 标注在 Controller 类上,跳过该类所有方法
        if (method.hasMethodAnnotation(NoAuthCheck.class) || method.getBeanType().isAnnotationPresent(NoAuthCheck.class)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // ===== 拦截条件:权限列表为空 =====
        // 用户没有分配任何权限,直接拒绝
        if (CollectionUtil.isEmpty(sysPermissionEntityList)) {
            writeUnauthorized(response, "没有权限");
            return false;
        }

        // ===== 放行条件 3:API 权限路径匹配 =====
        // 1. 先过滤出权限类型为 API 的权限(排除 MENU 和 BUTTON)
        // 2. 用 AntPathMatcher 将权限配置的路径与实际请求路径匹配
        //    例如:权限配置 /api/course/{id} 可以匹配实际请求 /api/course/123
        String requestURI = request.getRequestURI();
        boolean flag = sysPermissionEntityList.stream()
                .filter(sysPermissionEntity
                        -> sysPermissionEntity.getPermissionType().equals(PermissionTypeEnum.API.name()))
                .anyMatch(sysPermissionEntity -> PATH_MATCHER.match(sysPermissionEntity.getApiPath(), requestURI));

        // 没有任何 API 权限匹配当前请求路径,返回 403
        if (!flag) {
            writeUnauthorized(response, "暂无接口权限");
            return false;
        }

        // 所有校验通过,放行
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 返回 403 Forbidden 响应。
     * 注意:这里是 403(已认证但无权限),不是 401(未认证)。
     * 401 由 JwtAuthInterceptor 和 SignInterceptor 返回。
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiResponse<Void> error = ApiResponse.fail(403, message);
        response.getWriter().write(JSONObject.toJSONString(error));
    }
}
