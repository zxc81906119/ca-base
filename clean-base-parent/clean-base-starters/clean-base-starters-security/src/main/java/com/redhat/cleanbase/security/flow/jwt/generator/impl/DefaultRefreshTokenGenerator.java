package com.redhat.cleanbase.security.flow.jwt.generator.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;

public class DefaultRefreshTokenGenerator extends AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> {

    public DefaultRefreshTokenGenerator(JwtFlowProperties jwtProperties, DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(jwtProperties, delegateJwtKeyGetter);
    }

    @Override
    protected DefaultRefreshToken createToken() {
        return new DefaultRefreshToken();
    }

}