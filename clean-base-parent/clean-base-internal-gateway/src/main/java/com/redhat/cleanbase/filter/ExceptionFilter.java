package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstants;
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
@Order(OrderConstants.EXCEPTION_FILTER_ORDER)
@Component
public class ExceptionFilter implements GlobalFilter {

    private final Map<Class<? extends Throwable>, List<ExceptionHandler<?, ?>>> exceptionHandlerMap = new HashMap<>();

    public ExceptionFilter(List<ExceptionHandler<?, ?>> exceptionHandlers) {
        exceptionHandlers.forEach((exceptionHandler) -> {
            val processException = getProcessException(exceptionHandler);
            exceptionHandlerMap.computeIfAbsent(processException, (key) -> new ArrayList<>())
                    .add(exceptionHandler);
        });
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume((throwable) ->
                        getExceptionHandler(exchange, throwable)
                                .map((handler) ->
                                        Mono.defer(() -> handler.process(exchange, CastUtil.cast(throwable)))
                                                .onErrorMap((handlerThrowable) -> {
                                                    log.error("handler process occur exception", handlerThrowable);
                                                    return throwable;
                                                }))
                                .orElseGet(() -> Mono.error(throwable)));
    }

    private Optional<? extends ExceptionHandler<?, ?>> getExceptionHandler(ServerWebExchange serverWebExchange, Throwable throwable) {
        val throwableClass = throwable.getClass();
        val exceptionHandlers = exceptionHandlerMap.get(throwableClass);
        if (exceptionHandlers != null) {
            return getProcessExceptionHandler(exceptionHandlers, serverWebExchange, throwable);
        }
        synchronized (exceptionHandlerMap) {
            val handlerList = exceptionHandlerMap.get(throwableClass);
            if (handlerList != null) {
                return getProcessExceptionHandler(handlerList, serverWebExchange, throwable);
            }
            val candidateExceptionHandlers = getCandidateExceptionHandlers(throwable);
            candidateExceptionHandlers.ifPresentOrElse(
                    (exceptionHandlerList) -> exceptionHandlerMap.put(throwableClass, exceptionHandlerList),
                    () -> exceptionHandlerMap.put(throwableClass, List.of())
            );
            return candidateExceptionHandlers.flatMap(
                    (exceptionHandlerList) -> getProcessExceptionHandler(exceptionHandlerList, serverWebExchange, throwable)
            );
        }
    }

    private Optional<List<ExceptionHandler<?, ?>>> getCandidateExceptionHandlers(Throwable throwable) {
        return exceptionHandlerMap.entrySet().stream()
                .filter((exceptionHandlerEntry) ->
                        exceptionHandlerEntry.getKey().isInstance(throwable)
                )
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
                .map(Map.Entry::getValue);
    }

    private static Optional<ExceptionHandler<?, ?>> getProcessExceptionHandler(List<ExceptionHandler<?, ?>> exceptionHandlers, ServerWebExchange serverWebExchange, Throwable throwable) {
        return exceptionHandlers.stream()
                .filter((exceptionHandler) ->
                        exceptionHandler.isSupported(
                                serverWebExchange
                                , CastUtil.cast(throwable)
                        )
                )
                .findFirst();
    }

    private static Class<? extends Throwable> getProcessException(ExceptionHandler<?, ?> exceptionHandler) {
        return CastUtil.cast(
                Optional.of(exceptionHandler.getClass())
                        .map(Class::getGenericInterfaces)
                        .flatMap((types) ->
                                Arrays.stream(types)
                                        .filter(ParameterizedType.class::isInstance)
                                        .map(ParameterizedType.class::cast)
                                        .filter((parameterizedType) ->
                                                ExceptionHandler.class.equals(parameterizedType.getRawType()))
                                        .findFirst()
                        )
                        .map(ParameterizedType::getActualTypeArguments)
                        .filter((types) -> types.length != 0)
                        .map((types) -> types[0])
                        .filter(Class.class::isInstance)
                        .map(Class.class::cast)
                        .filter(Throwable.class::isAssignableFrom)
                        .orElseThrow()
        );
    }


}
