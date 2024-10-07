package com.redhat.cleanbase.security.flow.jwt.filter.service;

import com.redhat.cleanbase.security.flow.jwt.exception.UsernameConvertException;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class LoginAuthTokenUserDetailsService<T extends LoginAuthToken, U extends UserDetails> implements UserDetailsService {

    public U loadUserByAuthToken(T t) throws UsernameNotFoundException {
        try {
            val username = loginAuthTokenToUsername(t);
            return loadUserByUsername(username);
        } catch (UsernameConvertException e) {
            return realLoadUserByAuthToken(t);
        }
    }

    protected U realLoadUserByAuthToken(T t) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("override method if you want !!!");
    }

    protected String loginAuthTokenToUsername(T t) throws UsernameConvertException {
        val username = t.getName();
        if (StringUtils.isBlank(username)) {
            throw new UsernameConvertException();
        }
        return username;
    }

    @Override
    public abstract U loadUserByUsername(String username) throws UsernameNotFoundException;

}