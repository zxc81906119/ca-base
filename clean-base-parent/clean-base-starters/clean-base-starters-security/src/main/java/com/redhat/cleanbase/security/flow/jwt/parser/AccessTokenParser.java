package com.redhat.cleanbase.security.flow.jwt.parser;

import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;

public abstract class AccessTokenParser<T extends AccessToken> extends AbstractJwtParser<T> {
    public AccessTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(delegateJwtKeyGetter);
    }
}