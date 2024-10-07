package com.redhat.cleanbase.security.flow.jwt.filter.provider.impl;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.filter.provider.LoginProvider;
import com.redhat.cleanbase.security.flow.jwt.filter.service.LoginAuthTokenUserDetailsService;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultLoginProvider extends LoginProvider<LoginAuthToken, User> {

    public DefaultLoginProvider(PasswordEncoder passwordEncoder, AuthenticationAccessor<User> authenticationAccessor, LoginAuthTokenUserDetailsService<LoginAuthToken, User> loginAuthTokenUserDetailsService) {
        super(passwordEncoder, authenticationAccessor, loginAuthTokenUserDetailsService);
    }

    @Override
    protected Class<LoginAuthToken> getLoginAuthTokenClass() {
        return LoginAuthToken.class;
    }
}
