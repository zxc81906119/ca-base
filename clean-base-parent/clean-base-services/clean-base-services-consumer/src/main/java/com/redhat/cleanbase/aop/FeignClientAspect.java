package com.redhat.cleanbase.aop;

import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

@Aspect
public abstract class FeignClientAspect {

    @AfterThrowing(value = "com.redhat.cleanbase.aop.pointcut.FeignClientPointcuts.feignClient()", throwing = "ex")
    public void processFeignClientException(JoinPoint joinPoint, Exception ex) throws Exception {
        val proceedingJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        throw convertException(proceedingJoinPoint, ex);
    }

    protected abstract Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e);


    public static class Default extends FeignClientAspect {
        @Override
        protected Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e) {
            return e;
        }
    }
}
