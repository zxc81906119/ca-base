package com.redhat.cleanbase.constant;

import org.springframework.core.Ordered;

public final class OrderConstants {
    private OrderConstants() {
        throw new UnsupportedOperationException();
    }

    public static final int HIGHEST_PRECEDENCE_500 = Ordered.HIGHEST_PRECEDENCE + 500;

    public static final int EXCEPTION_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    public static final int DUPLICATED_REQUEST_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 20;
    public static final int AUTH_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 30;
    public static final int VERSION_CONTROL_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 40;
}
