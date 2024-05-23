package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstant;
import com.redhat.cleanbase.exception.annotation.CustomExceptionHandler;
import com.redhat.cleanbase.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.util.CastUtil;
import com.redhat.cleanbase.util.ReflectionUtil;
import io.micrometer.observation.annotation.Observed;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Observed
@Slf4j
@Order(OrderConstant.EXCEPTION_FILTER_ORDER)
@Component
@RequiredArgsConstructor
public class ExceptionFilter implements GlobalFilter {

    private final List<ExceptionHandler> exceptionHandlers;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume((throwable) ->
                        exceptionHandlers.stream()
                                .filter((exceptionHandler) -> getExceptionHandlerAnnotation(exceptionHandler)
                                        .map(customExceptionHandler -> customExceptionHandler.value().isInstance(throwable))
                                        .orElseGet(() -> throwable instanceof Exception)
                                )
                                .min(ExceptionFilter::compareExceptionHandler)
                                .map((handler) -> Mono.defer(() -> handler.process(exchange, throwable)))
                                .orElseGet(() -> Mono.error(throwable)));
    }

    private static int compareExceptionHandler(ExceptionHandler exceptionHandler, ExceptionHandler exceptionHandler1) {
        final Function<ExceptionHandler, Class<? extends Exception>> exceptionHandlerClassFunction =
                (handler) -> getExceptionHandlerAnnotation(handler)
                        .map(CustomExceptionHandler::value)
                        .orElseGet(() -> CastUtil.cast(Exception.class));
        return Comparator.comparing(exceptionHandlerClassFunction, (e1, e2) -> {
                    if (e1.equals(e2)) {
                        return 0;
                    }
                    return e1.isAssignableFrom(e2) ? 1 : -1;
                })
                .compare(exceptionHandler, exceptionHandler1);
    }

    private static Optional<CustomExceptionHandler> getExceptionHandlerAnnotation(@NonNull ExceptionHandler exceptionHandler) {
        return ReflectionUtil.findAnnotationOpt(exceptionHandler.getClass(), CustomExceptionHandler.class);
    }
}
