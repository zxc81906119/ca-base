package com.redhat.cleanbase.client;

import com.redhat.cleanbase.client.fallback.factory.CircuitBreakerFallBackFeignClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(
        contextId = "cbYes"
        , name="clean-base-services-producer"
        , url="${producer.url}"
        , fallbackFactory = CircuitBreakerFallBackFeignClientFactory.class
)
public interface CircuitBreakerFeignClient {

    //8083
    @RequestMapping(value ="/test")
    String test83();

}

