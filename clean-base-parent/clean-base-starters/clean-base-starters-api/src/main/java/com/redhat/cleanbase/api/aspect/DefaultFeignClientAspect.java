package com.redhat.cleanbase.api.aspect;

import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class DefaultFeignClientAspect extends FeignClientAspect{
    @Override
    protected Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e) {
        return e;
    }
}
