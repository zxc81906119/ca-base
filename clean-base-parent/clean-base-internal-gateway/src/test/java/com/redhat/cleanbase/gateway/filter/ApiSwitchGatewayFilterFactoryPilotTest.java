package com.redhat.cleanbase.gateway.filter;

import com.redhat.cleanbase.gateway.base.BaseGatewayTest;
import com.redhat.cleanbase.gateway.constant.ProfileConstants;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@ActiveProfiles(profiles = ProfileConstants.PILOT_PROFILE_NAME)
public class ApiSwitchGatewayFilterFactoryPilotTest extends BaseGatewayTest {

    @Autowired
    private ApiSwitchGatewayFilterFactory filterFactory;

    @Test
    public void filter_pilot() {
        val exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/endpoint")
        );
        val config = new ApiSwitchGatewayFilterFactory.Config();
        val result = filterFactory.apply(config)
                .filter(exchange, GATEWAY_FILTER_CHAIN);

        StepVerifier.create(result)
                .verifyComplete();
    }


}
