package com.redhat.cleanbase.web.tracing;

import com.redhat.cleanbase.common.wrapper.Wrapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TracerWrapper implements Wrapper<Tracer> {

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


    @Override
    public Tracer unwrap() {
        return tracer;
    }
}
