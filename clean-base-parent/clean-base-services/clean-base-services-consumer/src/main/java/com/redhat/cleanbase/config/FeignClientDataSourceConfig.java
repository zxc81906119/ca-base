package com.redhat.cleanbase.config;

import com.redhat.cleanbase.config.prop.FeignClientDataSourceProp;
import com.redhat.cleanbase.data.source.FeignClientDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(FeignClientDataSourceProp.class)
public class FeignClientDataSourceConfig {

    private final FeignClientDataSourceProp feignClientDataSourceProp;

    @Bean
    public FeignClientDataSource feignClientDataSource() {
        return new FeignClientDataSource(feignClientDataSourceProp.getIsInherited());
    }

}
