package com.junho.systemdesign.global.config;

import com.junho.systemdesign.global.interceptor.NotFoundInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NotFoundInterceptor())
                .addPathPatterns("/**"); // 모든 경로에 적용
    }

}
