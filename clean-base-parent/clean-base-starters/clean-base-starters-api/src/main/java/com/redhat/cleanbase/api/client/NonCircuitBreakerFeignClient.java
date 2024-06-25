package com.redhat.cleanbase.api.client;

import com.redhat.cleanbase.api.config.FeignClientConfigs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(
        contextId = "clean-base-services-provider-cb-no"
        , name = "clean-base-services-provider"
        , configuration = {
        FeignClientConfigs.DefaultFeignClientBuilderConfig.class
        ,}
)
public interface NonCircuitBreakerFeignClient {

    //8083
    @RequestMapping(value = "/test")
    String test83() throws Exception;

}
