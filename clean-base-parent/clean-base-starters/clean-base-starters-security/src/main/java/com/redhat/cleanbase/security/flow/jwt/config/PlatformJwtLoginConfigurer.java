package com.redhat.cleanbase.security.flow.jwt.config;

import com.redhat.cleanbase.security.flow.jwt.filter.BaseLoginFilter;
import lombok.NonNull;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PlatformJwtLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, PlatformJwtLoginConfigurer<H>, BaseLoginFilter<?>> {

    public PlatformJwtLoginConfigurer(@NonNull BaseLoginFilter<?> authenticationFilter) {
        super(authenticationFilter, null);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

}
