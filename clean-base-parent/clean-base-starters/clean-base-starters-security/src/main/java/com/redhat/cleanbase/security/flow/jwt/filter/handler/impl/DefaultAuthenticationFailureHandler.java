package com.redhat.cleanbase.security.flow.jwt.filter.handler.impl;

import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final RqDelegateExceptionHandler rqDelegateExceptionHandler;

    @Override
    public final void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        rqDelegateExceptionHandler.handleAndWriteRs(request, response, e);
    }
}