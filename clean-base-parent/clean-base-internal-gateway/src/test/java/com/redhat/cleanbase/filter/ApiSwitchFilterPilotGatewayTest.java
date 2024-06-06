package com.redhat.cleanbase.filter;

import com.redhat.cleanbase.base.BaseGatewayTest;
import com.redhat.cleanbase.constant.ProfileConstants;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@ActiveProfiles(profiles = ProfileConstants.PILOT_PROFILE_NAME)
public class ApiSwitchFilterPilotGatewayTest extends BaseGatewayTest {

    @Autowired
    private ApiSwitchFilter filter;

    @Test
    public void filter_pilot() {
        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );

        val result = filter.filter(exchange, GATEWAY_FILTER_CHAIN);

        StepVerifier.create(result)
                .verifyComplete();
    }


}
