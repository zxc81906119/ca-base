package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public abstract class BaseLoginFilter<T> extends UsernamePasswordAuthenticationFilter {

    @NonNull
    private final RequestDtoConverter<T> requestDtoConverter;

    @Setter
    protected boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        val loginAuthToken = rqToLoginAuthToken(request);

        setDetails(request, loginAuthToken);

        return getAuthenticationManager().authenticate(loginAuthToken);
    }

    @SneakyThrows
    protected LoginAuthToken rqToLoginAuthToken(HttpServletRequest request) {
        val requestDto = requestDtoConverter.convert(request);
        return rqDtoToLoginAuthToken(requestDto);
    }

    protected abstract LoginAuthToken rqDtoToLoginAuthToken(T t);

}
