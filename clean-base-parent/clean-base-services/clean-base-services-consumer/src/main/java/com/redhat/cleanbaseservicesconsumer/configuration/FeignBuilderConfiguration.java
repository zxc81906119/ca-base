package com.redhat.cleanbaseservicesconsumer.configuration;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

//@Configuration    //不能加，不然會都有這個
public class FeignBuilderConfiguration {
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}