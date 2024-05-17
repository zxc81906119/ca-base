package com.redhat.cleanbase.factory;

import com.redhat.cleanbase.service.FallbackService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TestFallbackFactory implements FallbackFactory<FallbackService> {

    @Override
    public FallbackService create(Throwable cause) {
        return new FallbackService();
    }

}
