package com.redhat.cleanbase.exception.handler;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ExceptionHandler {
    Mono<Void> process(ServerWebExchange exchange, Throwable throwable);
}