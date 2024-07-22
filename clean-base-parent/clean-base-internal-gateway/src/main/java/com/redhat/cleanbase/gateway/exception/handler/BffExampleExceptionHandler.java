package com.redhat.cleanbase.gateway.exception.handler;

import com.redhat.cleanbase.gateway.exception.ExampleException;
import com.redhat.cleanbase.gateway.exception.handler.base.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class BffExampleExceptionHandler implements BaseExceptionHandler<ExampleException, Map<String, Object>> {

    public static final String ERR_MSG = "errMsg";

    @Override
    public boolean isSupported(ServerWebExchange exchange, ExampleException throwable) {
        return exchange.getRequest().getPath().elements().stream()
                .filter(PathContainer.PathSegment.class::isInstance)
                .findFirst()
                .map(PathContainer.Element::value)
                .map((firstPath) -> firstPath.startsWith("bff"))
                .orElse(false);
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> getResponseEntity(ServerWebExchange exchange, ExampleException throwable) {
        val data = Map.<String, Object>of(ERR_MSG, "bff");
        return Mono.just(new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
