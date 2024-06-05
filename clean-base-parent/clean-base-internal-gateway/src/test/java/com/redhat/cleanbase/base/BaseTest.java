package com.redhat.cleanbase.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;

@SpringBootTest
@DirtiesContext
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    @MockBean
    protected GatewayFilterChain filterChain;

    @BeforeEach
    public void eachSetup() {
        Mockito.doReturn(Mono.empty())
                .when(filterChain)
                .filter(Mockito.any());
    }
}
