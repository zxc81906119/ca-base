package com.redhat.cleanbase.security.flow.jwt.parser.impl;

import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.parser.AccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;

public class DefaultAccessTokenParser extends AccessTokenParser<DefaultAccessToken> {

    public DefaultAccessTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(delegateJwtKeyGetter);
    }

    @Override
    protected DefaultAccessToken createToken() {
        return new DefaultAccessToken();
    }
}