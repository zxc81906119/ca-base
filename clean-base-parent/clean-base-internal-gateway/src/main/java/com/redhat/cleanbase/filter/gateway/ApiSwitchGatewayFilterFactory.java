package com.redhat.cleanbase.filter.gateway;

import com.redhat.cleanbase.constant.ProfileConstants;
import com.redhat.cleanbase.exception.ExampleException;
import com.redhat.cleanbase.util.ReactiveUtil;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Observed
@RequiredArgsConstructor
@Slf4j
@Component
public class ApiSwitchGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiSwitchGatewayFilterFactory.Config> {

    private final Environment environment;

    @Override
    public GatewayFilter apply(Config config) {

        return (serverWebExchange, filterChain) -> {

            if (environment.acceptsProfiles(ProfileConstants.PILOT_PROFILES)) {
                return filterChain.filter(serverWebExchange);
            }

            return Mono.defer(this::findEnabledFlag)
                    .map(ApiSwitchGatewayFilterFactory::isEnabled)
                    .flatMap((enabled) ->
                            enabled ? filterChain.filter(serverWebExchange) :
                                    ReactiveUtil.callFuncAndGetMono(() -> {
                                        throw new ExampleException();
                                    })
                    );
        };
    }

    public Mono<String> findEnabledFlag() {
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

    public static class Config {

    }


}
