package com.redhat.cleanbase.security.flow.jwt.filter.handler.impl;

import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    private final RqDelegateExceptionHandler rqDelegateExceptionHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        rqDelegateExceptionHandler.handleAndWriteRs(request, response, accessDeniedException);
    }
}
