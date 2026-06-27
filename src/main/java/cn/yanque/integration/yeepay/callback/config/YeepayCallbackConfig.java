package cn.yanque.integration.yeepay.callback.config;

import cn.yanque.integration.yeepay.callback.YeepayCallbackServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "payment", name = "provider", havingValue = "yeepay")
public class YeepayCallbackConfig {

    @Bean
    public ServletRegistrationBean<YeepayCallbackServlet> yeepayCallbackServletRegistration() {
        ServletRegistrationBean<YeepayCallbackServlet> registration =
                new ServletRegistrationBean<>(new YeepayCallbackServlet(), YeepayCallbackServlet.CALLBACK_PREFIX + "/*");
        registration.setName("yeepayCallbackServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
