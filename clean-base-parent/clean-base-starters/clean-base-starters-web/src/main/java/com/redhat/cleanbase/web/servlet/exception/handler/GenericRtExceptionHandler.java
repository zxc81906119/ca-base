package com.redhat.cleanbase.web.servlet.exception.handler;

import com.redhat.cleanbase.exception.base.GenericRtException;
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
public class GenericRtExceptionHandler implements RqExceptionCondition<GenericRtException> {

    private final GenericExceptionFamilyHandler genericExceptionFamilyHandler;

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, GenericRtException e) {
        return genericExceptionFamilyHandler.handle(e);
    }

    @Override
    public Class<GenericRtException> getIdentifier() {
        return GenericRtException.class;
    }
}
