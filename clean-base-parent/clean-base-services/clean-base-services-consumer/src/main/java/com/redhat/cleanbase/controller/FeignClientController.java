package com.redhat.cleanbase.controller;

import com.redhat.cleanbase.client.NonCircuitBreakerFeignClient;
import com.redhat.cleanbase.client.CircuitBreakerFeignClient;
import com.redhat.cleanbase.data.DefaultFeignClientData;
import com.redhat.cleanbase.data.source.FeignClientDataSource;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeignClientController {

    private final CircuitBreakerFeignClient circuitBreakerFeignClient;
    private final NonCircuitBreakerFeignClient nonCircuitBreakerFeignClient;
    private final FeignClientDataSource feignClientDataSource;

    @RequestMapping("/testProducer")
    public String testProducer() {
        feignClientDataSource.setData(new DefaultFeignClientData());
        val response = circuitBreakerFeignClient.test83();
        return response;
    }

    @RequestMapping("/testProducerNoCircuitBreaker")
    public String testProducerNoCircuitBreaker() {
        val response = nonCircuitBreakerFeignClient.test83();
        return response;
    }

}
