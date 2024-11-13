package com.redhat.cleanbase.web.servlet.exception.handler;

import com.redhat.cleanbase.web.model.response.GenericResponse;
import com.redhat.cleanbase.web.servlet.utils.WebUtils;
import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.LinkedHashMap;

@Slf4j
@RequiredArgsConstructor
public class DefaultExceptionHandler implements RqExceptionCondition<Exception> {

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request, Exception e) {
        log.error("Exception: {}", e.getMessage(), e);

        val rsBody = new LinkedHashMap<String, Object>();
        rsBody.put("timestamp", new Timestamp(System.currentTimeMillis()));
        rsBody.put("path",
                WebUtils.getRequestURI()
                        .orElse(null)
        );

        return GenericResponse.response(ResponseCodeEnum.API_FAILED, rsBody);
    }

    @Override
    public Class<Exception> getIdentifier() {
        return Exception.class;
    }
}
