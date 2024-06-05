package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.base.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@ActiveProfiles(profiles = "pilot")
public class ApiSwitchFilterPilotTest extends BaseTest {

    @Autowired
    private ApiSwitchFilter filter;

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
