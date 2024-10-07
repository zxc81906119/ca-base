package com.redhat.cleanbase.security.flow.jwt.parser;

import com.redhat.cleanbase.security.flow.jwt.exception.JwtParseAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;

public interface JwtParser<T extends JwtToken> {
    T parse(String jwt) throws JwtParseAuthenticationException;
}