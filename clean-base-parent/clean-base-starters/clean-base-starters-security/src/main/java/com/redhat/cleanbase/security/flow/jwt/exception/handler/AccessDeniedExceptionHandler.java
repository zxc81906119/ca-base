package com.redhat.cleanbase.security.flow.jwt.exception.handler;

import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

@Slf4j
public class AccessDeniedExceptionHandler implements RqExceptionCondition<AccessDeniedException> {

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, AccessDeniedException e) {
        log.error("AccessDenied Exception : {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @Override
    public Class<AccessDeniedException> getIdentifier() {
        return AccessDeniedException.class;
    }
}
