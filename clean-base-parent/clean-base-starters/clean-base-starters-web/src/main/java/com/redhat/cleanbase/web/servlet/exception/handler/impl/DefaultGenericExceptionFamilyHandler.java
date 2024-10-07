package com.redhat.cleanbase.web.servlet.exception.handler.impl;

import com.redhat.cleanbase.exception.base.GenericExceptionNamespace;
import com.redhat.cleanbase.exception.handler.GenericExceptionFamilyHandler;
import com.redhat.cleanbase.web.model.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;

@Slf4j
public class DefaultGenericExceptionFamilyHandler implements GenericExceptionFamilyHandler {

    @Override
    public ResponseEntity<?> handle(GenericExceptionNamespace<?> gEN) {
        val e = gEN.toSelf();

        log.error("Generic Exception : {}", e.getMessage(), e);

        val content = gEN.getContent();

        return GenericResponse.response(content.getResponseCode(), content.getDetails());
    }

}
