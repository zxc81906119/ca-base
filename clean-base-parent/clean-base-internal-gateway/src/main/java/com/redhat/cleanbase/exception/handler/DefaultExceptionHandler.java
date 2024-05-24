package com.redhat.cleanbase.exception.handler;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class DefaultExceptionHandler implements ExceptionHandler<Exception> {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, Exception exception) {
        return Mono.fromRunnable(() -> {
            System.out.println("do nothing for exception");
            val response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
