package com.gzu.common.security.config;

import com.gzu.common.security.filter.AuthRequestInterceptor;
import com.gzu.common.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class WebMvcSecurityConfiguration implements WebMvcConfigurer {
    private final SecurityProperties properties;

    public WebMvcSecurityConfiguration(SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthRequestInterceptor(properties))
                .addPathPatterns("/**");
    }
}

