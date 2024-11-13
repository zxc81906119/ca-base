package com.redhat.cleanbase.security.flow.jwt.generator;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.AccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;

public abstract class AbstractAccessTokenGenerator<T extends AccessToken, TS extends AccessTokenDataSource> extends AbstractJwtTokenGenerator<T, TS> {

    public AbstractAccessTokenGenerator(JwtFlowProperties jwtProperties, DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(jwtProperties, delegateJwtKeyGetter);
    }

    @Override
    protected JwtFlowProperties.TimeProperties getExpireTimeInfo(TS ts) {
        return jwtProperties.getAccessTokenProperties()
                .getTimeout();
    }

}
