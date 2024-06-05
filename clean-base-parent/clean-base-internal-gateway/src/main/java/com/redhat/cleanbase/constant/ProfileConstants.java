package com.redhat.cleanbase.constant;

import org.springframework.core.env.Profiles;

public final class ProfileConstants {
    private ProfileConstants() {
        throw new UnsupportedOperationException();
    }

    public static final Profiles PILOT_PROFILES = Profiles.of("pilot");

}
