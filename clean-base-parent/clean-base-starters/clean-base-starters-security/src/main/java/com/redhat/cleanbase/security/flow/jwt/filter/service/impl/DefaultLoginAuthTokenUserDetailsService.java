package com.redhat.cleanbase.security.flow.jwt.filter.service.impl;

import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.security.flow.jwt.filter.service.LoginAuthTokenUserDetailsService;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DefaultLoginAuthTokenUserDetailsService extends LoginAuthTokenUserDetailsService<LoginAuthToken, User> {
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return CastUtils.cast(
                User.builder()
                        .username("haha")
                        .password("password")
                        .build(),
                User.class);
    }
}
