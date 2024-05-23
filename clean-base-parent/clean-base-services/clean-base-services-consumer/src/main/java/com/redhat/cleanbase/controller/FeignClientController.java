package com.redhat.cleanbase.controller;

import com.redhat.cleanbase.service.FeignClientNoCircuitBreakerService;
import com.redhat.cleanbase.service.FeignClientService;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignClientController {
    @Resource
    private FeignClientService feignClientService;
    @Resource
    private FeignClientNoCircuitBreakerService feignClientNoCircuitBreakerService;

    @RequestMapping("/testProducer")
    public String testProducer() {
        val response = feignClientService.test83();
        return response;
    }

    @RequestMapping("/testProducerNoCircuitBreaker")
    public String testProducerNoCircuitBreaker() {
        val response = feignClientNoCircuitBreakerService.test83();
        return response;
    }

}