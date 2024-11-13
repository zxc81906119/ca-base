package com.redhat.cleanbase.actuator.health;

import lombok.val;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        val errorCode = check();
        if (errorCode != 0) {
            builder.down()
                    .withDetail("Error Code", errorCode);
        } else {
            builder.up();
        }
    }

    private int check() {
        // perform some specific health check
        return 4;
    }

}