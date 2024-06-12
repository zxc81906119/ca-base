package com.redhat.cleanbase.exception.handler;

import com.redhat.cleanbase.exception.ExampleException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;

@Slf4j
@Component
public class ExampleExceptionHandler implements ExceptionHandler<ExampleException, Map<String, Object>> {

    public static final String ERR_MSG = "errMsg";

    @Override
    public ResponseEntity<Map<String, Object>> getResponseEntity(ServerWebExchange exchange, ExampleException throwable) {
        val message = throwable.getMessage();
        val data = Map.<String, Object>of(ERR_MSG, message);
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
