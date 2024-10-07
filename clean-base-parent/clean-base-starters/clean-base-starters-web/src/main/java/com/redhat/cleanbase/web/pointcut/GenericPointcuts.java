package com.redhat.cleanbase.web.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public final class GenericPointcuts {
    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut() {
    }
}
