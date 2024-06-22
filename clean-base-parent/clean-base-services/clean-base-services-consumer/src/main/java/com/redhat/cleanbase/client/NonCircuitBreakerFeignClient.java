package com.redhat.cleanbase.client;

import com.redhat.cleanbase.config.FeignClientConfigs;
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
