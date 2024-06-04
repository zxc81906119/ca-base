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
        // todo 不知道需不需要包裝例外
        // todo 如需要就要 onErrorResume
        // todo 不需要就這樣
        return findEnabledFlag()
                .map(ApiSwitchFilter::isEnabled)
                .flatMap((enabled) ->
                        enabled ? chain.filter(exchange)
                                : getExceptionMono());
    }


    public static Mono<Void> getExceptionMono() {
        try {
            throwException();
        } catch (ExampleException e) {
            return Mono.error(e);
        }
        // 這邊不會有機會進來
        return Mono.empty();
    }

    public static Mono<String> findEnabledFlag() {
        return Mono.defer(() ->
                Mono.fromFuture(
                        // todo 從 db 抓資料
                        CompletableFuture.supplyAsync(() ->
                                "gg"
                        )
                )
        );
    }

    private static boolean isEnabled(String enabledFlag) {
        return Boolean.TRUE.toString().equalsIgnoreCase(enabledFlag);
    }

    // todo 模擬 action exception,
    // todo 將 action exception handler 邏輯
    // todo 拿來 gateway exception handler 處理
    public static class ExampleException extends Exception {
    }

    public static void throwException() throws ExampleException {
        throw new ExampleException();
    }

}
