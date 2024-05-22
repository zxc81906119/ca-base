package com.redhat.cleanbase.constant;

import org.springframework.core.Ordered;

public final class OrderConstant {
    private OrderConstant() {
        throw new UnsupportedOperationException();
    }

    public static final int HIGHEST_PRECEDENCE_500 = Ordered.HIGHEST_PRECEDENCE + 500;

    public static final int EXCEPTION_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    public static final int API_SWITCH_FILTER_ORDER = HIGHEST_PRECEDENCE_500;
    public static final int DUPLICATED_REQUEST_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 1;
    public static final int AUTH_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 2;
    public static final int VERSION_CONTROL_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 3;
    public static final int DEVICE_ID_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 4;
    public static final int CIPHER_FILTER_ORDER = HIGHEST_PRECEDENCE_500 + 5;
}
