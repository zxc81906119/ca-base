package com.redhat.cleanbase.security.flow.jwt.filter.entrypoint;

import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RqDelegatingExceptionHandler rqDelegatingExceptionHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        rqDelegatingExceptionHandler.handleAndWriteRs(request, response, authException);
    }
}
