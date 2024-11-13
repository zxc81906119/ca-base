package com.redhat.cleanbase.web.servlet.exception.handler;

import com.redhat.cleanbase.web.servlet.exception.condition.RqExceptionCondition;
import com.redhat.cleanbase.exception.handler.AbstractDelegatingExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class RqDelegatingExceptionHandler extends AbstractDelegatingExceptionHandler<HttpServletRequest, ResponseEntity<?>> {

    private final RsEntityRsWriter writeRsEntityToRs;

    public RqDelegatingExceptionHandler(List<RqExceptionCondition<?>> exceptionHandlers, RsEntityRsWriter writeRsEntityToRs) {
        super(exceptionHandlers);
        this.writeRsEntityToRs = writeRsEntityToRs;
    }

    public void handleAndWriteRs(HttpServletRequest request, HttpServletResponse response, Exception e) {
        writeRsEntityToRs.lazyWriteWithSupplier(response)
                .accept(() -> handle(request, e));
    }
}
