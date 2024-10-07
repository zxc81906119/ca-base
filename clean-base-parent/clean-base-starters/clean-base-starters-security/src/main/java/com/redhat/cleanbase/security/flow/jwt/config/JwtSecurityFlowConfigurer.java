package com.redhat.cleanbase.security.flow.jwt.config;

import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.exception.SecurityPropValidationException;
import com.redhat.cleanbase.security.flow.config.SecurityFlowConfigurer;
import com.redhat.cleanbase.security.flow.jwt.annotation.JwtSecurityFlow;
import com.redhat.cleanbase.security.flow.jwt.filter.BaseLoginFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.provider.LoginProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
@ConditionalOnMissingBean(JwtSecurityFlowConfigurer.class)
@JwtSecurityFlow
@Configuration
@Import(JwtSecurityFlowInnerConfigurer.class)
public class JwtSecurityFlowConfigurer implements SecurityFlowConfigurer {
    
    private final LogoutHandler logoutHandler;
    private final LoginProvider<?, ?> loginProvider;
    private final BaseLoginFilter<?> baseLoginFilter;

    @Override
    public void validateProperties(SecurityConfigProperties properties) throws SecurityPropValidationException {
        val jwtFlowProperties = properties.getJwtFlowProperties();
        val authorizeRequestProperties = properties.getAuthorizeRequestProperties();

        val loginUri = jwtFlowProperties.getLoginUri();

        val requestMatchAuthPropertiesSet = authorizeRequestProperties.getRequestMatchAuthPropertiesList();
        if (requestMatchAuthPropertiesSet == null) {
            throw new SecurityPropValidationException("authorizeRequestProperties.getRequestMatchAuthPropertiesSet() can't be null");
        }

        val noneMatchLoginUri = requestMatchAuthPropertiesSet.stream()
                .noneMatch((requestMatchAuthProperties) -> {
                    val urls = requestMatchAuthProperties.getUrls();
                    val permitAll = requestMatchAuthProperties.getPermitAll();
                    return urls != null
                            && urls.contains(loginUri)
                            && Boolean.TRUE.equals(permitAll);
                });
        if (noneMatchLoginUri) {
            throw new SecurityPropValidationException("authorizeRequestProperties.getRequestMatchAuthPropertiesSet() must have config loginUri :%s and permit all".formatted(loginUri));
        }
    }

    @Override
    public void config(HttpSecurity security, SecurityConfigProperties properties) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        (securitySessionManagementConfigurer) ->
                                securitySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout((logoutConfigurer) -> {
                    logoutConfigurer.addLogoutHandler(logoutHandler);
                })
                .authenticationProvider(loginProvider)
                .addFilterBefore(baseLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
