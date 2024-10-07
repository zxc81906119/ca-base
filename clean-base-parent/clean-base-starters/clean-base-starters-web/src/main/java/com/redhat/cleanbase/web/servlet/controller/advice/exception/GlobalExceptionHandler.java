package com.redhat.cleanbase.web.servlet.controller.advice.exception;

import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final RqDelegateExceptionHandler rqDelegateExceptionHandler;

    @ExceptionHandler(Exception.class)
    public void handlerException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        rqDelegateExceptionHandler.handleAndWriteRs(request, response, e);
    }
}
