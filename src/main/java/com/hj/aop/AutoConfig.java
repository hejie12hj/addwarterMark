package com.hj.aop;

import com.hj.AddWaterMarkUtil;
import com.hj.core.AddWaterMark;
import com.hj.interceptor.AddWaterMarkInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnProperty(value = {"addWaterMark.enable"}, havingValue = "true")
@Configuration
public class AutoConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAddWaterMarkInterceptor()).addPathPatterns("/**");
    }

    @Bean
    HandlerInterceptor getAddWaterMarkInterceptor() {
        return new AddWaterMarkInterceptor(getAddWaterMark());
    }

    @Bean
    AddWaterMark getAddWaterMark() {
        return new AddWaterMarkUtil();
    }

    @Bean
    @ConditionalOnProperty(value = {"addWaterMark.aop"}, havingValue = "true")
    public Advisor addWaterMarkAdvisor() {
        return new AddWaterMarkAdvisor(getAddWaterMark());
    }


}