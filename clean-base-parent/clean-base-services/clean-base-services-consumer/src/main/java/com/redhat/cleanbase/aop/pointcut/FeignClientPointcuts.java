package com.redhat.cleanbase.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class FeignClientPointcuts {

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void feignClient() {
    }

}
