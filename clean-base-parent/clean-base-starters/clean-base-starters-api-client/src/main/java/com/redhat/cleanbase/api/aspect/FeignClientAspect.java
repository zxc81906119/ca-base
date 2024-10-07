package com.redhat.cleanbase.api.aspect;

import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

@Aspect
public abstract class FeignClientAspect {

    @AfterThrowing(value = "com.redhat.cleanbase.api.aspect.pointcut.FeignClientPointcuts.feignClient()", throwing = "ex")
    public void processFeignClientException(JoinPoint joinPoint, Exception ex) throws Exception {
        val proceedingJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        throw convertException(proceedingJoinPoint, ex);
    }

    protected abstract Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e);
}
