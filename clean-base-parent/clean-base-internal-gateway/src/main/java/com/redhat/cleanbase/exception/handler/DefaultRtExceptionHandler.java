package com.redhat.cleanbase.exception.handler;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class DefaultRtExceptionHandler implements ExceptionHandler<RuntimeException> {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, RuntimeException runtimeException) {
        return Mono.fromRunnable(() -> {
            System.out.println("do nothing for rt exception");
            val response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
