package com.redhat.cleanbase.security.flow.jwt.exception.handler;

import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class AuthenticationExceptionHandler implements RqExceptionCondition<AuthenticationException> {


    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, AuthenticationException e) {
        log.error("Authentication Exception : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @Override
    public Class<AuthenticationException> getIdentifier() {
        return AuthenticationException.class;
    }
}
