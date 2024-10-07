package com.redhat.cleanbase.api.client;

import com.redhat.cleanbase.api.client.fallback.factory.CircuitBreakerFallBackFeignClientFactory;
import com.redhat.cleanbase.api.config.FeignClientConfigs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(
        contextId = "clean-base-services-provider-cb-yes"
        , name = "clean-base-services-provider"
        , fallbackFactory = CircuitBreakerFallBackFeignClientFactory.class
        , configuration = {
        FeignClientConfigs.TaskContextToRequestHeaderInterceptorConfig.class
        ,
}
)
public interface CircuitBreakerFeignClient {

    //8083
    @RequestMapping(value = "/test83")
    String test83() throws Exception;

}

