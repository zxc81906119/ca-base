package com.redhat.cleanbase.security.flow.jwt.filter.provider;

import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.filter.service.LoginAuthTokenUserDetailsService;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public abstract class LoginProvider<T extends LoginAuthToken, U extends UserDetails> implements AuthenticationProvider {

    private final Class<T> loginAuthTokenClass = Objects.requireNonNull(getLoginAuthTokenClass());

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationAccessor<U> authenticationAccessor;
    private final LoginAuthTokenUserDetailsService<T, U> loginAuthTokenUserDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return loginAuthTokenClass.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return realAuthenticate(loginAuthTokenClass.cast(authentication));
    }

    protected Authentication realAuthenticate(T t) {
        val u = loginAuthTokenUserDetailsService.loadUserByAuthToken(t);
        if (u == null) {
            throw new UsernameNotFoundException("使用者不存在");
        }
        if (!checkCredentials(t, u)) {
            throw new BadCredentialsException("密碼驗證失敗");
        }
        return authenticationAccessor.createAuthentication(u);
    }

    protected boolean checkCredentials(T t, U u) {
        val credentials = t.getCredentials();
        if (credentials == null) {
            return false;
        }
        return passwordEncoder.matches(credentials.toString(), u.getPassword());
    }

    protected abstract Class<T> getLoginAuthTokenClass();


}