package com.redhat.cleanbase.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.cleanbase.exception.ExampleException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class ExampleExceptionHandler implements ExceptionHandler<ExampleException> {

    public static final String ERROR_MESSAGE = "errorMessage";

    @Override
    public Mono<Void> process(ServerWebExchange exchange, ExampleException throwable) {
        log.error("do nothing for example exception", throwable);

        val response = exchange.getResponse();

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        val message = throwable.getMessage();

        val data = Map.of(ERROR_MESSAGE, message);

        try {
            val dataBuffer = getResponseDataBuffer(data);
            return response.writeWith(dataBuffer);
        } catch (JsonProcessingException e) {
            log.error("transfer to response data failed", e);
            return Mono.error(e);
        }
    }

    public static <T> Mono<DataBuffer> getResponseDataBuffer(T data) throws JsonProcessingException {
        val dataBufferFactory = new DefaultDataBufferFactory();
        val responseData = new ObjectMapper().writeValueAsBytes(data);
        val dataBuffer = dataBufferFactory.wrap(responseData);
        return Mono.just(dataBuffer);
    }

}
