package com.redhat.cleanbase.security.flow.jwt.parser;

import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;

public abstract class RefreshTokenParser<T extends RefreshToken> extends AbstractJwtParser<T> {
    public RefreshTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        super(delegateJwtKeyGetter);
    }
}