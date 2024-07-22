package com.redhat.cleanbase.gateway.base;

import com.redhat.cleanbase.test.base.BaseTest;
import lombok.val;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public abstract class BaseGatewayTest extends BaseTest {

    protected static final GatewayFilterChain GATEWAY_FILTER_CHAIN = getDefaultMockGatewayFilterChain();

    private static GatewayFilterChain getDefaultMockGatewayFilterChain() {
        return getMockGatewayFilterChain(Mono.empty());
    }

    public static GatewayFilterChain getMockGatewayFilterChain(Mono<Void> voidMono) {
        val filterChain = Mockito.mock(GatewayFilterChain.class);
        Mockito.doReturn(voidMono)
                .when(filterChain)
                .filter(Mockito.any(ServerWebExchange.class));
        return filterChain;
    }

}
