package cn.yanque.config;

import cn.yanque.models.auth.interceptor.JwtAuthInterceptor;
import cn.yanque.models.auth.interceptor.PendingPaySignInterceptor;
import cn.yanque.models.auth.interceptor.PermissionInterceptor;
import cn.yanque.models.auth.interceptor.SignInterceptor;
import cn.yanque.models.auth.interceptor.StudentJwtAuthInterceptor;
import cn.yanque.models.auth.interceptor.StudentSignInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthInterceptor jwtAuthInterceptor;

    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Autowired
    private SignInterceptor signInterceptor;

    @Autowired
    private PendingPaySignInterceptor pendingPaySignInterceptor;

    @Autowired
    private StudentJwtAuthInterceptor studentJwtAuthInterceptor;

    @Autowired
    private StudentSignInterceptor studentSignInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器按注册顺序执行：先解析 JWT 得到 userId，再验签，最后做接口权限判断。
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/sysUser/login",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**");

        // 登录接口还没有 token 和签名密钥，所以需要排除；Swagger 也排除，方便本地调试接口文档。
        registry.addInterceptor(signInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/sysUser/login",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**");

        // 权限拦截只控制业务 API，登录和接口文档不进入权限校验。
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/sysUser/login",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**");

        registry.addInterceptor(pendingPaySignInterceptor)
                .addPathPatterns("/student/pending/**");

        registry.addInterceptor(studentJwtAuthInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns(
                        "/student/login",
                        "/student/pending/**");

        registry.addInterceptor(studentSignInterceptor)
                .addPathPatterns("/student/**")
                .excludePathPatterns(
                        "/student/login",
                        "/student/pending/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
