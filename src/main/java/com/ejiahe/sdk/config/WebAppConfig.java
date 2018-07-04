package com.ejiahe.sdk.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2017/3/22.
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(WebAppConfig.class, args);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ConfigInterceptor())
//                .addPathPatterns("/*","/*/*","/*/*/*","/*/*/*/*","/*/*/*/*/*")
//                .excludePathPatterns("/index")
//                .excludePathPatterns("/save");
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger/**");
//    }

    /**
     * 允许跨域访问
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
    }
}
