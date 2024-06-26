package com.redhat.cleanbase.consumer.common.aspect;

import com.redhat.cleanbase.api.aspect.FeignClientAspect;
import com.redhat.cleanbase.api.data.source.FeignClientDataSource;
import com.redhat.cleanbase.consumer.common.exception.ActionException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionExceptionFeignClientAspect extends FeignClientAspect {

    private final FeignClientDataSource feignClientDataSource;

    @Override
    protected Exception convertException(MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint, Exception e) {
        val data = feignClientDataSource.getData();
        if (data == null) {
            return new ActionException("no data and occur exception");
        }
        return new ActionException(data.getSystemId(), "has data and occur exception");
    }
}