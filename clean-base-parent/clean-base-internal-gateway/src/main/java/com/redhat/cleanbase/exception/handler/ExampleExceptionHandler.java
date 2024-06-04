package com.redhat.cleanbase.exception.handler;

import com.redhat.cleanbase.filter.ApiSwitchFilter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ExampleExceptionHandler implements ExceptionHandler<ApiSwitchFilter.ExampleException> {
    @Override
    public Mono<Void> process(ServerWebExchange exchange, ApiSwitchFilter.ExampleException throwable) {
        return Mono.fromRunnable(() -> {
            log.info("do nothing for exception");
            val response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        });
    }
}
