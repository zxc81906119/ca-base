package com.redhat.cleanbase.exception.handler;

import com.redhat.cleanbase.exception.annotation.CustomExceptionHandler;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@CustomExceptionHandler(RuntimeException.class)
public class DefaultRtExceptionHandler implements ExceptionHandler {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, Throwable throwable) {
        return Mono.fromRunnable(() -> {
            System.out.println("do nothing for rt exception");
            val response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
