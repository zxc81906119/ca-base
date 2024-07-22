package com.redhat.cleanbase.gateway.filter.global;

import com.redhat.cleanbase.gateway.constant.OrderConstants;
import com.redhat.cleanbase.gateway.exception.handler.base.BaseExceptionHandler;
import com.redhat.cleanbase.gateway.util.CastUtils;
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

    private final Map<Class<? extends Throwable>, List<BaseExceptionHandler<?, ?>>> exceptionHandlerMap = new HashMap<>();

    public ExceptionFilter(List<BaseExceptionHandler<?, ?>> exceptionHandlers) {
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
                                        Mono.defer(() -> handler.process(exchange, CastUtils.cast(throwable)))
                                                .onErrorMap((handlerThrowable) -> {
                                                    log.error("handler process occur exception", handlerThrowable);
                                                    return throwable;
                                                }))
                                .orElseGet(() -> Mono.error(throwable)));
    }

    private Optional<? extends BaseExceptionHandler<?, ?>> getExceptionHandler(ServerWebExchange exchange, Throwable throwable) {
        return exceptionHandlerMap.entrySet().stream()
                .filter((exceptionHandlerEntry) ->
                        exceptionHandlerEntry.getKey().isInstance(throwable)
                )
                // 對 候選的 entry 做排序,最靠近處理例外的例外處理者優先
                .sorted(
                        Map.Entry.comparingByKey(
                                (e1, e2) -> {
                                    if (e1.equals(e2)) {
                                        return 0;
                                    }
                                    return e1.isAssignableFrom(e2) ? 1 : -1;
                                }
                        )
                )
                // 每個 entry 轉成例外處理者
                .map(Map.Entry::getValue)
                // 攤平成所有例外處理者一份
                .flatMap(Collection::stream)
                .filter((exceptionHandler) ->
                        exceptionHandler.isSupported(
                                exchange
                                , CastUtils.cast(throwable)
                        )
                )
                .findFirst();
    }

    private static Class<? extends Throwable> getProcessException(BaseExceptionHandler<?, ?> exceptionHandler) {
        return CastUtils.cast(
                Optional.of(exceptionHandler.getClass())
                        .map(Class::getGenericInterfaces)
                        .flatMap((types) ->
                                Arrays.stream(types)
                                        .filter(ParameterizedType.class::isInstance)
                                        .map(ParameterizedType.class::cast)
                                        .filter((parameterizedType) ->
                                                BaseExceptionHandler.class.equals(parameterizedType.getRawType()))
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
