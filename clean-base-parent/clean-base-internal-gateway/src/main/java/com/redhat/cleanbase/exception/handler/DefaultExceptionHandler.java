package com.redhat.cleanbase.exception.handler;

import com.redhat.cleanbase.exception.annotation.CustomExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@CustomExceptionHandler
@Component
public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, Throwable throwable) {
        return Mono.empty();
    }
}
