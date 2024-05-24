package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstant;
import com.redhat.cleanbase.exception.annotation.CustomExceptionHandler;
import com.redhat.cleanbase.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.util.CastUtil;
import com.redhat.cleanbase.util.ReflectionUtil;
import io.micrometer.observation.annotation.Observed;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@Observed
@Slf4j
@Order(OrderConstant.EXCEPTION_FILTER_ORDER)
@Component
public class ExceptionFilter implements GlobalFilter {

    private final Map<Class<? extends Throwable>, ExceptionHandler> exceptionHandlerMap = new HashMap<>();

    public ExceptionFilter(List<ExceptionHandler> exceptionHandlers) {
        exceptionHandlers.forEach((exceptionHandler) -> {
            val processException = getExceptionHandlerAnnotation(exceptionHandler)
                    .map(CustomExceptionHandler::value)
                    .orElseGet(() -> CastUtil.cast(Exception.class));
            if (exceptionHandlerMap.get(processException) != null) {
                throw new UnsupportedOperationException("不支援同一個例外有多個處理器!!!");
            }
            exceptionHandlerMap.put(processException, exceptionHandler);
        });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume((throwable) ->
                        Optional.ofNullable(exceptionHandlerMap.get(throwable.getClass()))
                                .or(() ->
                                        exceptionHandlerMap.entrySet().stream()
                                                .filter((exceptionHandlerEntry) ->
                                                        exceptionHandlerEntry.getKey().isInstance(throwable))
                                                .min(
                                                        Map.Entry.comparingByKey(
                                                                (e1, e2) -> {
                                                                    if (e1.equals(e2)) {
                                                                        return 0;
                                                                    }
                                                                    return e1.isAssignableFrom(e2) ? 1 : -1;
                                                                }
                                                        )
                                                )
                                                .map(Map.Entry::getValue)
                                )
                                .map((handler) -> Mono.defer(() -> handler.process(exchange, throwable)))
                                .orElseGet(() -> Mono.error(throwable)));
    }

    private static Optional<CustomExceptionHandler> getExceptionHandlerAnnotation(@NonNull ExceptionHandler exceptionHandler) {
        return ReflectionUtil.findAnnotationOpt(exceptionHandler.getClass(), CustomExceptionHandler.class);
    }
}
