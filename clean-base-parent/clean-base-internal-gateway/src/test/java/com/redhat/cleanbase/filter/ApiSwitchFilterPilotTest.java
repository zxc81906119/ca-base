package com.redhat.cleanbase.filter;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ActiveProfiles(profiles = "pilot")
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ApiSwitchFilterPilotTest {

    @SpyBean
    private ApiSwitchFilter filter;

    @MockBean
    private GatewayFilterChain filterChain;

    @BeforeEach
    public void eachSetup() {
        Mockito.doReturn(Mono.empty())
                .when(filterChain)
                .filter(Mockito.any());
    }

    @Test
    public void filter_pilot() {
        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );

        val result = filter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();
    }


}
