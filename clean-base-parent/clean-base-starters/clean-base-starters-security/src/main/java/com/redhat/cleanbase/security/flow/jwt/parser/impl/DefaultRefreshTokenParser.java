package com.redhat.cleanbase.security.flow.jwt.parser.impl;

import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.parser.RefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;

public class DefaultRefreshTokenParser extends RefreshTokenParser<DefaultRefreshToken> {

    public DefaultRefreshTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(delegateJwtKeyGetter);
    }

    @Override
    protected DefaultRefreshToken createToken() {
        return new DefaultRefreshToken();
    }
}