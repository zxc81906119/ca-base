package com.redhat.cleanbase.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Slf4j
public class DefaultRtExceptionHandler implements ExceptionHandler<RuntimeException, Map<String, Object>> {

    @Override
    public ResponseEntity<Map<String, Object>> getResponseEntity(ServerWebExchange exchange, RuntimeException throwable) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
