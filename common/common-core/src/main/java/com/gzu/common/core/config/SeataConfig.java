package com.gzu.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.seata.spring.annotation.datasource.SeataAutoDataSourceProxyCreator;

@Configuration
public class SeataConfig {

    @Bean
    public SeataAutoDataSourceProxyCreator seataAutoDataSourceProxyCreator() {
        return new SeataAutoDataSourceProxyCreator(true, new String[]{}, "AT");
    }
}
