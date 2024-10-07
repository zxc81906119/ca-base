package com.redhat.cleanbase.api.client.fallback.factory;

import com.redhat.cleanbase.api.client.fallback.CircuitBreakerFallBackFeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CircuitBreakerFallBackFeignClientFactory implements FallbackFactory<CircuitBreakerFallBackFeignClient> {

    @Override
    public CircuitBreakerFallBackFeignClient create(Throwable cause) {
        return new CircuitBreakerFallBackFeignClient();
    }

}
