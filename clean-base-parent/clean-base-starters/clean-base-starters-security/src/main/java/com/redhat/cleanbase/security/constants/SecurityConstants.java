package com.redhat.cleanbase.security.constants;

public final class SecurityConstants {

    public static final String SECURITY_PROP_PREFIX = "platform.security";
    public static final String JWT_PROP_PREFIX = SECURITY_PROP_PREFIX + "." + "jwt";

    private SecurityConstants() {
        throw new UnsupportedOperationException();
    }

}
