package com.redhat.cleanbase.security.flow.jwt.generator.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultAccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;

import java.util.Optional;
import java.util.UUID;

public class DefaultAccessTokenGenerator extends AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> {

    public DefaultAccessTokenGenerator(JwtFlowProperties jwtProperties, DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(jwtProperties, delegateJwtKeyGetter);
    }

    @Override
    protected DefaultAccessToken createToken() {
        return new DefaultAccessToken();
    }

    @Override
    protected String getSubject(DefaultAccessTokenDataSource dataSource) {
        return dataSource.getUserId();
    }

    @Override
    protected String getId(DefaultAccessTokenDataSource ts) {
        return Optional.ofNullable(ts.getJwtId())
                .orElseGet(() -> UUID.randomUUID().toString());
    }

}
