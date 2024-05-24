package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstant;
import com.redhat.cleanbase.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.util.CastUtil;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.*;

@Observed
@Slf4j
@Order(OrderConstant.EXCEPTION_FILTER_ORDER)
@Component
public class ExceptionFilter implements GlobalFilter {

    private final Map<Class<? extends Throwable>, ExceptionHandler<?>> exceptionHandlerMap = new HashMap<>();

    public ExceptionFilter(List<ExceptionHandler<?>> exceptionHandlers) {
        exceptionHandlers.forEach((exceptionHandler) -> {
            val processException = getProcessException(exceptionHandler);
            if (exceptionHandlerMap.get(processException) != null) {
                throw new UnsupportedOperationException("不支援同一個例外有多個處理器!!!");
            }
            exceptionHandlerMap.put(processException, exceptionHandler);
        });
    }

    private static Class<? extends Throwable> getProcessException(ExceptionHandler<?> exceptionHandler) {
        return CastUtil.cast(
                Optional.of(exceptionHandler.getClass().getGenericSuperclass())
                        .filter(ParameterizedType.class::isInstance)
                        .map(ParameterizedType.class::cast)
                        .map(ParameterizedType::getActualTypeArguments)
                        .filter((types) -> types.length != 0)
                        .map((types) -> types[0])
                        .filter(Class.class::isInstance)
                        .map(Class.class::cast)
                        .filter(Throwable.class::isAssignableFrom)
                        .orElse(Throwable.class)
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume((throwable) ->
                        getExceptionHandler(throwable)
                                .map((handler) -> Mono.defer(() -> handler.process(exchange, CastUtil.cast(throwable))))
                                .orElseGet(() -> Mono.error(throwable)));
    }

    private Optional<? extends ExceptionHandler<?>> getExceptionHandler(Throwable throwable) {
        return Optional.ofNullable(exceptionHandlerMap.get(throwable.getClass()))
                .or(() ->
                        exceptionHandlerMap.entrySet().stream()
                                .filter((exceptionHandlerEntry) ->
                                        exceptionHandlerEntry.getKey().isInstance(throwable))
                                .min(Map.Entry.comparingByKey(
                                        (e1, e2) -> {
                                            if (e1.equals(e2)) {
                                                return 0;
                                            }
                                            return e1.isAssignableFrom(e2) ? 1 : -1;
                                        }
                                ))
                                .map(Map.Entry::getValue)
                                .map(CastUtil::cast)
                );
    }

}
