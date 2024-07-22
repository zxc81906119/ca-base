package com.redhat.cleanbase.gateway.exception.handler.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


public interface BaseExceptionHandler<T extends Throwable, K> {

    Logger LOGGER = LoggerFactory.getLogger(BaseExceptionHandler.class);

    default boolean isSupported(ServerWebExchange exchange, T throwable) {
        return true;
    }

    default Mono<Void> process(ServerWebExchange exchange, T throwable) {

        val classSimpleName = getClass().getSimpleName();

        LOGGER.error("[handler: {}] process exception...", classSimpleName, throwable);

        return Mono.defer(() -> getResponseEntity(exchange, throwable))
                .flatMap((responseEntity) -> {

                    val response = exchange.getResponse();

                    val statusCode = responseEntity.getStatusCode();
                    response.setStatusCode(statusCode);

                    val headers = responseEntity.getHeaders();
                    val responseHeaders = response.getHeaders();
                    responseHeaders.putAll(headers);

                    return Optional.ofNullable(responseEntity.getBody())
                            .map((body) ->
                                    getDataBuffer(response.bufferFactory(), body)
                                            .transform(response::writeWith)
                            )
                            .orElseGet(Mono::empty);
                });
    }

    default Mono<DataBuffer> getDataBuffer(DataBufferFactory dataBufferFactory, K data) {
        try {
            val responseData = getObjMapper().writeValueAsBytes(data);
            return Mono.just(dataBufferFactory.wrap(responseData));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    default ObjectMapper getObjMapper() {
        return new ObjectMapper();
    }

    Mono<ResponseEntity<K>> getResponseEntity(ServerWebExchange exchange, T throwable);

}