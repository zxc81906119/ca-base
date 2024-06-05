package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.base.BaseTest;
import com.redhat.cleanbase.exception.ExampleException;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.doReturn;

public class ApiSwitchFilterNonPilotTest extends BaseTest {

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
    public void filter_non_pilot_enable() {

        doReturn(Mono.just(Boolean.TRUE.toString()))
                .when(filter)
                .findEnabledFlag();

        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );

        val result = filter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    public void filter_non_pilot_not_enable() {

        doReturn(Mono.just(Boolean.FALSE.toString()))
                .when(filter)
                .findEnabledFlag();

        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );

        val result = filter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyError(ExampleException.class);

    }


}
