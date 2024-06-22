package com.redhat.cleanbase.controller;

import com.redhat.cleanbase.client.NonCircuitBreakerFeignClient;
import com.redhat.cleanbase.client.CircuitBreakerFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeignClientController {

    private final CircuitBreakerFeignClient circuitBreakerFeignClient;
    private final NonCircuitBreakerFeignClient nonCircuitBreakerFeignClient;

    @RequestMapping("/testProducer")
    public String testProducer() {
        val response = circuitBreakerFeignClient.test83();
        return response;
    }

    @RequestMapping("/testProducerNoCircuitBreaker")
    public String testProducerNoCircuitBreaker() {
        val response = nonCircuitBreakerFeignClient.test83();
        return response;
    }

}
