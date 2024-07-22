package com.redhat.cleanbase.gateway.filter;

import com.redhat.cleanbase.gateway.base.BaseGatewayTest;
import com.redhat.cleanbase.gateway.exception.ExampleException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.doReturn;

public class ApiSwitchGatewayFilterFactoryNonPilotTest extends BaseGatewayTest {

    @SpyBean
    private ApiSwitchGatewayFilterFactory filterFactory;

    @Test
    public void filter_non_pilot_enable() {

        doReturn(Mono.just(Boolean.TRUE.toString()))
                .when(filterFactory)
                .findEnabledFlag();

        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );
        val config = new ApiSwitchGatewayFilterFactory.Config();
        val result = filterFactory.apply(config)
                .filter(exchange, GATEWAY_FILTER_CHAIN);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    public void filter_non_pilot_not_enable() {

        doReturn(Mono.just(Boolean.FALSE.toString()))
                .when(filterFactory)
                .findEnabledFlag();

        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );
        val config = new ApiSwitchGatewayFilterFactory.Config();
        val result = filterFactory.apply(config)
                .filter(exchange, GATEWAY_FILTER_CHAIN);

        StepVerifier.create(result)
                .verifyError(ExampleException.class);

    }


}
