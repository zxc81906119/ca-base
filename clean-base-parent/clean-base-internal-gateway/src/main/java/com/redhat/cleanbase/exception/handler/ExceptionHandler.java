package com.redhat.cleanbase.exception.handler;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ExceptionHandler<T extends Throwable> {
    Mono<Void> process(ServerWebExchange exchange, T throwable);
}