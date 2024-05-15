package com.redhat.cleanbase.common.exception.handler;

import com.redhat.cleanbase.common.exception.generic.GenericException;
import com.redhat.cleanbase.common.exception.generic.GenericExceptionInterface;
import com.redhat.cleanbase.common.exception.generic.GenericRtException;
import com.redhat.cleanbase.common.response.GenericResponse;
import com.redhat.cleanbase.common.trace.TraceManagement;
import com.redhat.cleanbase.common.util.WebUtil;
import io.micrometer.observation.annotation.Observed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Observed
public class GlobalExceptionHandler {

    private final TraceManagement traceManagement;

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<GenericResponse<Map<String, Object>>> processException(GenericException genericException) {
        return getGenericResponseEntity(genericException);
    }

    @ExceptionHandler(GenericRtException.class)
    public ResponseEntity<GenericResponse<Map<String, Object>>> processException(GenericRtException genericRtException) {
        return getGenericResponseEntity(genericRtException);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public GenericResponse<Map<String, Object>> processException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        val dataMap = getDataMap();
        return GenericResponse.fail(dataMap, traceManagement.getCurrentSpanTraceId());
    }

    private ResponseEntity<GenericResponse<Map<String, Object>>> getGenericResponseEntity(GenericExceptionInterface<? extends Exception> e) {
        val exception = e.toRealType();

        log.error("Generic Exception: {}", e.getMessage(), exception);

        val content = e.getContent();

        val dataMap = getDataMap(content.getDetails());

        return GenericResponse.response(content.getCode(), dataMap, traceManagement.getCurrentSpanTraceId());
    }

    private Map<String, Object> getDataMap(Map<String, Object> details) {
        val hm = new LinkedHashMap<String, Object>();

        hm.put("timestamp", new Timestamp(System.currentTimeMillis()));

        val requestUri = WebUtil.getHttpServletRequest()
                .map(HttpServletRequest::getRequestURI)
                .orElse(null);
        hm.put("path", requestUri);

        hm.put("details", Optional.ofNullable(details)
                .orElseGet(Map::of));

        return hm;
    }

    private Map<String, Object> getDataMap() {
        return getDataMap(null);
    }

}
