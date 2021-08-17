package com.cn.lx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Web Mvc 配置
 * @author StevenLu
 * @date 2021/8/17 下午10:38
 */
@Configuration
public class CommerceWebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor())
                .addPathPatterns("/**").order(0);
    }
}
