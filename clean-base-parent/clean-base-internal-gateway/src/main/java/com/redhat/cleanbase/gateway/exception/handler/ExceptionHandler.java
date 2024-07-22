package com.redhat.cleanbase.gateway.exception.handler;

import com.redhat.cleanbase.gateway.exception.handler.base.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class ExceptionHandler implements BaseExceptionHandler<Exception, Map<String, Object>> {
    @Override
    public Mono<ResponseEntity<Map<String, Object>>> getResponseEntity(ServerWebExchange exchange, Exception throwable) {
        return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
