package com.redhat.cleanbase.gateway.constant;

import org.springframework.core.Ordered;

public final class OrderConstants {
    private OrderConstants() {
        throw new UnsupportedOperationException();
    }

    public static final int HIGHEST_PRECEDENCE_500 = Ordered.HIGHEST_PRECEDENCE + 500;

    public static final int EXCEPTION_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;
}
