package com.redhat.cleanbase.provider.controller;

import com.redhat.cleanbase.api.client.CircuitBreakerFeignClient;
import com.redhat.cleanbase.api.data.DefaultFeignClientData;
import com.redhat.cleanbase.api.proxy.FeignClientProxy;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Observed
@Slf4j
public class FeignServerController {

    private final FeignClientProxy feignClientProxy;

    private final CircuitBreakerFeignClient circuitBreakerFeignClient;

    @RequestMapping("/test")
    public String test() throws Exception {
        val feignClientData = new DefaultFeignClientData("fuck");
        val proxy = feignClientProxy.proxy(circuitBreakerFeignClient, feignClientData);
        return proxy.test83();
    }

}
