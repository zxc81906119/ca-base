package com.redhat.cleanbase.security.flow.jwt.exception.handler;

import ch.qos.logback.core.joran.spi.ActionException;
import com.redhat.cleanbase.web.servlet.exception.condition.ExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ActionExceptionHandler implements ExceptionCondition<HttpServletRequest, ActionException> {
    @Override
    public Class<ActionException> getIdentifier() {
        return ActionException.class;
    }

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, ActionException e) {
        log.error("Authentication Exception : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
    }
}
