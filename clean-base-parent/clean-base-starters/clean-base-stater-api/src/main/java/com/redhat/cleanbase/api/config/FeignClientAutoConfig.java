package com.redhat.cleanbase.api.config;

import com.redhat.cleanbase.api.aop.FeignClientAspect;
import com.redhat.cleanbase.api.client.proxy.FeignClientProxy;
import com.redhat.cleanbase.api.config.prop.FeignClientDataSourceProp;
import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan({"com.redhat.cleanbase.api.client"})
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

    @Bean
    public FeignClientProxy feignClientProxy() {
        return new FeignClientProxy(feignClientDataSource());
    }

}
