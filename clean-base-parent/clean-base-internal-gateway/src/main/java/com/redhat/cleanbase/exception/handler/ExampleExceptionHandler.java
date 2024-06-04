package com.redhat.cleanbase.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.cleanbase.filter.ApiSwitchFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Slf4j
@Component
public class ExampleExceptionHandler implements ExceptionHandler<ApiSwitchFilter.ExampleException> {
    @SneakyThrows
    @Override
    public Mono<Void> process(ServerWebExchange exchange, ApiSwitchFilter.ExampleException throwable) {
        log.info("do nothing for example exception");

        val response = exchange.getResponse();

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        val dataBufferFactory = new DefaultDataBufferFactory();

        val responseMap = new HashMap<String, String>();
        responseMap.put("message", throwable.getMessage());

        val responseData = new ObjectMapper()
                .writeValueAsBytes(responseMap);

        return response.writeWith(Mono.just(dataBufferFactory.wrap(responseData)));
    }
}
