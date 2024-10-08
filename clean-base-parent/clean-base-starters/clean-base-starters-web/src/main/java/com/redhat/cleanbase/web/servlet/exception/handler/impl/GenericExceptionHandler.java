package com.redhat.cleanbase.web.servlet.exception.handler.impl;

import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.exception.handler.GenericExceptionFamilyHandler;
import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GenericExceptionHandler implements RqExceptionCondition<GenericException> {

    private final GenericExceptionFamilyHandler genericExceptionFamilyHandler;

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, GenericException e) {
        return genericExceptionFamilyHandler.handle(e);
    }

    @Override
    public Class<GenericException> getIdentifier() {
        return GenericException.class;
    }

}
