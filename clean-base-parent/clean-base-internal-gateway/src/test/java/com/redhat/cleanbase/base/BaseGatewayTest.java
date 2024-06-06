package com.redhat.cleanbase.base;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

public abstract class BaseGatewayTest extends BaseTest {

    @MockBean
    protected GatewayFilterChain filterChain;

    @BeforeEach
    public void eachSetup() {
        Mockito.doReturn(Mono.empty())
                .when(filterChain)
                .filter(Mockito.any());
    }
}
