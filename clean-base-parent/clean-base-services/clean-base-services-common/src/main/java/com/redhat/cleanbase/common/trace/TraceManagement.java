package com.redhat.cleanbase.common.trace;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TraceManagement {

    private final Tracer tracer;

    public String getCurrentSpanTraceId() {
        return getCurrentSpanTraceIdOrDefault(null);
    }

    public String getCurrentSpanTraceIdOrDefault(String defaultValue) {
        return getCurrentSpanTraceIdOpt()
                .orElse(defaultValue);
    }

    public Optional<String> getCurrentSpanTraceIdOpt() {
        return getSpanTraceIdOpt(tracer.currentSpan());
    }

    public Optional<String> getSpanTraceIdOpt(Span span) {
        return getSpanContextOpt(span)
                .map(TraceContext::traceId);
    }

    public Optional<TraceContext> getSpanContextOpt(Span span) {
        return Optional.ofNullable(span)
                .map(Span::context);
    }


}
