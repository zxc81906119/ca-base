package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.constant.OrderConstants;
import com.redhat.cleanbase.constant.ProfileConstants;
import com.redhat.cleanbase.exception.ExampleException;
import com.redhat.cleanbase.util.ReactiveUtil;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
@Order(OrderConstants.API_SWITCH_FILTER_ORDER)
public class ApiSwitchFilter implements GlobalFilter {

    private final Environment environment;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (environment.acceptsProfiles(ProfileConstants.PILOT_PROFILES)) {
            return chain.filter(exchange);
        }

//        if (true) {
//            return Mono.error(new ExampleException());
//        }

        return Mono.defer(this::findEnabledFlag)
                .map(ApiSwitchFilter::isEnabled)
                .flatMap((enabled) ->
                        enabled ? chain.filter(exchange) :
                                ReactiveUtil.callFuncAndGetMono(() -> {
                                    throw new ExampleException();
                                })
                );
    }

    public Mono<String> findEnabledFlag() {
        // todo 從 db 抓資料
        return Mono.fromFuture(
                CompletableFuture.supplyAsync(
                        () -> {
                            try {
                                Thread.sleep(4000);
                            } catch (InterruptedException e) {
                                log.error("[findEnabledFlag] interrupted exception", e);
                            }
                            return "true";
                        }
                ));
    }

    private static boolean isEnabled(String enabledFlag) {
        return Boolean.TRUE.toString().equalsIgnoreCase(enabledFlag);
    }


}
