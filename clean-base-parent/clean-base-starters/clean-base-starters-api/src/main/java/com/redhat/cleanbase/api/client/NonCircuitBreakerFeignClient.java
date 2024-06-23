package com.redhat.cleanbase.api.client;

import com.redhat.cleanbase.api.config.FeignClientConfigs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(
        contextId = "cbNo"
        , name = "clean-base-services-producer"
        , url = "${producer.url}"
        , configuration = {
                FeignClientConfigs.DefaultFeignClientBuilderConfig.class
        ,}
)
public interface NonCircuitBreakerFeignClient {

    //8083
    @RequestMapping(value = "/test")
    String test83();

}
