package com.redhat.cleanbase.web.servlet.exception.handler.impl;

import com.redhat.cleanbase.web.servlet.exception.condition.ExceptionCondition;
import com.redhat.cleanbase.web.servlet.exception.handler.AbstractDelegateExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.WriteRsEntityToRsProcessor;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class RqDelegateExceptionHandler extends AbstractDelegateExceptionHandler<HttpServletRequest> {

    public RqDelegateExceptionHandler(List<ExceptionCondition<HttpServletRequest, ?>> exceptionHandlers, WriteRsEntityToRsProcessor writeRsEntityToRs) {
        super(exceptionHandlers, writeRsEntityToRs);
    }
}
