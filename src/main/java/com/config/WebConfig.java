package com.config;

import com.filter.InviteCodeInterceptor;
import com.filter.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getInviteCodeInterceptor(){
        return new InviteCodeInterceptor();
    }


    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/workload/login");

        registry.addInterceptor(getInviteCodeInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/workload/login", "/workload/judgeInviteCode");
    }
}