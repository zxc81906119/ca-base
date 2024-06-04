package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstant;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
@Order(OrderConstant.API_SWITCH_FILTER_ORDER)
public class ApiSwitchFilter implements GlobalFilter {

    private final Environment environment;

    public static final String SPRING_PROFILE_PILOT = "pilot";

    public static final Profiles PILOT_PROFILES = Profiles.of(SPRING_PROFILE_PILOT);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (environment.acceptsProfiles(PILOT_PROFILES)) {
            return chain.filter(exchange);
        }
        // todo 如需要包 error to action error , 擇加上 onErrorResume
        return Mono.defer(ApiSwitchFilter::findEnabledFlag)
                .map(ApiSwitchFilter::isEnabled)
                .flatMap((enabled) ->
                        enabled ? chain.filter(exchange) :
                                getExceptionMono(() -> {
                                    throw new ExampleException();
                                })
                );
    }

    public static <O> Mono<O> getExceptionMono(Callable<O> callable) {
        try {
            return Optional.ofNullable(callable.call())
                    .map(Mono::just)
                    .orElseGet(Mono::empty);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public static Mono<String> findEnabledFlag() {
        // todo 從 db 抓資料
        return Mono.fromFuture(
                CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                Thread.sleep(4000);
                                throw new RuntimeException("hello");
                            } catch (InterruptedException e) {
                            }
                            return "true";
                        }
                ));
    }

    private static boolean isEnabled(String enabledFlag) {
        return Boolean.TRUE.toString().equalsIgnoreCase(enabledFlag);
    }

    // todo 模擬 action exception,
    // todo 將 action exception handler 邏輯
    // todo 拿來 gateway exception handler 處理
    public static class ExampleException extends Exception {
    }


}
