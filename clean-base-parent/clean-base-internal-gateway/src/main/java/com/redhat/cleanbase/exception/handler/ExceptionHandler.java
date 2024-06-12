package com.redhat.cleanbase.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public interface ExceptionHandler<T extends Throwable, K> {

    Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    default Mono<Void> process(ServerWebExchange exchange, T throwable) {

        val classSimpleName = getClass().getSimpleName();

        LOGGER.error("[handler: {}] process exception...", classSimpleName, throwable);

        val response = exchange.getResponse();

        val responseEntity = getResponseEntity(exchange, throwable);

        val statusCode = responseEntity.getStatusCode();
        response.setStatusCode(statusCode);

        val headers = responseEntity.getHeaders();
        val responseHeaders = response.getHeaders();
        responseHeaders.putAll(headers);

        try {
            val body = responseEntity.getBody();
            if (body != null) {
                val dataBuffer = getMonoDataBuffer(body);
                return response.writeWith(dataBuffer);
            }
            return Mono.empty();
        } catch (JsonProcessingException e) {
            LOGGER.error("parse body exception...", e);
            return Mono.error(throwable);
        }
    }

    default Mono<DataBuffer> getMonoDataBuffer(K data) throws JsonProcessingException {

        val responseData = new ObjectMapper()
                .writeValueAsBytes(data);

        val dataBufferFactory = new DefaultDataBufferFactory();

        val dataBuffer = dataBufferFactory
                .wrap(responseData);

        return Mono.just(dataBuffer);
    }

    ResponseEntity<K> getResponseEntity(ServerWebExchange exchange, T throwable);

}