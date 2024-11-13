package com.redhat.cleanbase.security.flow.jwt.filter.handler.impl;

import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final RqDelegatingExceptionHandler rqDelegatingExceptionHandler;

    @Override
    public final void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        rqDelegatingExceptionHandler.handleAndWriteRs(request, response, e);
    }
}