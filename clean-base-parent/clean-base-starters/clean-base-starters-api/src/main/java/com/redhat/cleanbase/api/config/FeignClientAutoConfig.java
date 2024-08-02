package com.redhat.cleanbase.api.config;

import com.redhat.cleanbase.api.annotation.EnablePowerFeignClients;
import com.redhat.cleanbase.api.aspect.DefaultFeignClientAspect;
import com.redhat.cleanbase.api.aspect.FeignClientAspect;
import com.redhat.cleanbase.api.config.prop.FeignClientDataSourceProp;
import com.redhat.cleanbase.api.data.source.DefaultFeignClientDataSource;
import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import com.redhat.cleanbase.api.proxy.FeignClientProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@Configuration
@ComponentScan("com.redhat.cleanbase.api.client")
@EnablePowerFeignClients("com.redhat.cleanbase.api.client")
@EnableConfigurationProperties(FeignClientDataSourceProp.class)
@Import(TaskContextConfig.class)
public class FeignClientAutoConfig {

    private final FeignClientDataSourceProp feignClientDataSourceProp;

    @Bean
    @ConditionalOnMissingBean
    public FeignClientDataSource feignClientDataSource() {
        return new DefaultFeignClientDataSource(feignClientDataSourceProp.getIsInherited());
    }

    @Bean
    @ConditionalOnMissingBean
    public FeignClientAspect feignClientAspect() {
        return new DefaultFeignClientAspect();
    }

    @Bean
    public FeignClientProxy feignClientProxy(FeignClientDataSource feignClientDataSource) {
        return new FeignClientProxy(feignClientDataSource);
    }

}
