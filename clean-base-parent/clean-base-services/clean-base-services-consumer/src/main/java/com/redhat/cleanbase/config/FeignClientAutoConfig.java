package com.redhat.cleanbase.config;

import com.redhat.cleanbase.aop.FeignClientAspect;
import com.redhat.cleanbase.config.prop.FeignClientDataSourceProp;
import com.redhat.cleanbase.data.source.FeignClientDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan({"com.redhat.cleanbase.client"})
@EnableConfigurationProperties(FeignClientDataSourceProp.class)
@RequiredArgsConstructor
@Configuration
public class FeignClientAutoConfig {

    private final FeignClientDataSourceProp feignClientDataSourceProp;

    @Bean
    public FeignClientDataSource feignClientDataSource() {
        return new FeignClientDataSource(feignClientDataSourceProp.getIsInherited());
    }

    @Bean
    @ConditionalOnMissingBean
    public FeignClientAspect feignClientAspect() {
        return new FeignClientAspect.Default();
    }

}
