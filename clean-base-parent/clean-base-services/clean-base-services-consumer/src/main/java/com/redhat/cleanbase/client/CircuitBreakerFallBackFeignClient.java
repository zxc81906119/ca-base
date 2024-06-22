package com.redhat.cleanbase.client;

import com.redhat.cleanbase.client.fallback.CircuitBreakerFeignClient;

public class CircuitBreakerFallBackFeignClient implements CircuitBreakerFeignClient {

    //8083
    @Override
    public String test83() {
        return "No test83";
    }

}
