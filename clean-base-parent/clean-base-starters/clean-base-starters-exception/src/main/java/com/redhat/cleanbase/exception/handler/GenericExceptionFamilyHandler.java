package com.redhat.cleanbase.exception.handler;

import com.redhat.cleanbase.exception.base.GenericExceptionNamespace;
import org.springframework.http.ResponseEntity;

public interface GenericExceptionFamilyHandler {

    ResponseEntity<?> handle(GenericExceptionNamespace<?> genericExceptionNamespace);

}
