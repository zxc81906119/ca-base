package com.redhat.cleanbase.security.flow.jwt.filter.model.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginAuthToken extends UsernamePasswordAuthenticationToken {

    public LoginAuthToken(Object principal, Object credentials,
                          Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public LoginAuthToken(Object principal, Object credentials) {
        this(principal, credentials, null);
    }
}
