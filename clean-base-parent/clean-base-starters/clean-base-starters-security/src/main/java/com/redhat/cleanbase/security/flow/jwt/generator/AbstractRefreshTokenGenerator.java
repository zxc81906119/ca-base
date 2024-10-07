package com.redhat.cleanbase.security.flow.jwt.generator;

import com.redhat.cleanbase.security.config.properties.TimeProperties;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.RefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import lombok.val;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractRefreshTokenGenerator<T extends RefreshToken, TS extends RefreshTokenDataSource> extends AbstractJwtTokenGenerator<T, TS> {

    public static final String IS_REFRESH_TOKEN = "IS_REFRESH_TOKEN";

    public AbstractRefreshTokenGenerator(JwtFlowProperties jwtProperties, DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(jwtProperties, delegateJwtKeyGetter);
    }

    @Override
    protected TimeProperties getExpireTimeInfo(TS ts) {
        return jwtProperties.getRefreshTokenProperties()
                .getTimeout();
    }

    @Override
    protected String getId(TS ts) {
        return ts.getAccessToken()
                .getId();
    }

    @Override
    protected String getSubject(TS ts) {
        return ts.getAccessToken()
                .getSubject();
    }

    @Override
    protected Map<String, Object> getClaims(TS ts) {

        val data = new HashMap<String, Object>();

        val payload = ts.getAccessToken().getPayload();

        Optional.ofNullable(payload)
                .ifPresent(data::putAll);

        data.put(IS_REFRESH_TOKEN, true);

        return data;
    }

}
