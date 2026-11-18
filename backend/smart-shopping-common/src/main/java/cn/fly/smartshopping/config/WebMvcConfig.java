package cn.fly.smartshopping.config;

import cn.fly.smartshopping.security.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册权限拦截器。
     *
     * 跨域配置统一交给 Gateway 处理，
     * 微服务本身不再重复添加 CORS 响应头。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionInterceptor())
                .addPathPatterns("/**");
    }
}
