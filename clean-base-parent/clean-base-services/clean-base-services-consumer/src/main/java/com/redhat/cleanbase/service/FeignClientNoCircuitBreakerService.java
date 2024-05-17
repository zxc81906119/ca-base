package com.redhat.cleanbase.service;

import com.redhat.cleanbase.config.FeignBuilderConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(contextId="cbNo", name="clean-base-services-producer", url="${producer.url}", configuration = FeignBuilderConfiguration.class)
public interface FeignClientNoCircuitBreakerService {

    //8083
    @RequestMapping(value ="/test")
    String test83();

}
