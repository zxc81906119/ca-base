package com.redhat.cleanbase.controller;

import com.redhat.cleanbase.api.client.CircuitBreakerFeignClient;
import com.redhat.cleanbase.api.client.proxy.FeignClientProxy;
import com.redhat.cleanbase.api.data.FeignClientData;
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
    public String test() {
        val feignClientData = new FeignClientData.Default();
        val proxy = feignClientProxy.proxy(circuitBreakerFeignClient, feignClientData);
        return proxy.test83();
    }

}
