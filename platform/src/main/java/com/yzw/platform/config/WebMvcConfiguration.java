package com.yzw.platform.config;

import com.yzw.platform.interceptor.UrlHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private UrlHandlerInterceptor urlHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*InterceptorRegistration interceptorRegistration = registry.addInterceptor(urlHandlerInterceptor).addPathPatterns("/**");
        String excludePath = "/,/error,/security/**,/ace/**";
        interceptorRegistration.excludePathPatterns(excludePath.split(",")) ;*/
    }
}
