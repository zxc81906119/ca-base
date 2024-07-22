package com.redhat.cleanbase.gateway.constant;

import org.springframework.core.env.Profiles;

public final class ProfileConstants {
    private ProfileConstants() {
        throw new UnsupportedOperationException();
    }

    public static final String PILOT_PROFILE_NAME = "pilot";

    public static final Profiles PILOT_PROFILES = Profiles.of(PILOT_PROFILE_NAME);

}
