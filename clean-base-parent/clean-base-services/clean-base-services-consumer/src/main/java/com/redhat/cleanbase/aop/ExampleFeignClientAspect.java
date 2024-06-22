package com.redhat.cleanbase.aop;

import com.redhat.cleanbase.aop.base.BaseFeignClientAspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class ExampleFeignClientAspect extends BaseFeignClientAspect {

    @Override
    protected Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e) {
        return new IllegalArgumentException(e.getMessage());
    }
}
