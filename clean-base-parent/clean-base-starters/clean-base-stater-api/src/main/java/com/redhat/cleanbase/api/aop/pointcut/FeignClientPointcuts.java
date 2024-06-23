package com.redhat.cleanbase.api.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class FeignClientPointcuts {

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void feignClient() {
    }

}
