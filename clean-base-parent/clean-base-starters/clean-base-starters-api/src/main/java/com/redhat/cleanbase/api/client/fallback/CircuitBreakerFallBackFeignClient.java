package com.redhat.cleanbase.api.client.fallback;


import com.redhat.cleanbase.api.client.CircuitBreakerFeignClient;

public class CircuitBreakerFallBackFeignClient implements CircuitBreakerFeignClient {

    //8083
    @Override
    public String test83() {
        return "No test83";
    }

}
