package com.redhat.cleanbase.exception.handler;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class DefaultExceptionHandler implements ExceptionHandler<Exception> {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, Exception exception) {
        return Mono.fromRunnable(() -> {
            log.info("do nothing for exception", exception);
            val response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
