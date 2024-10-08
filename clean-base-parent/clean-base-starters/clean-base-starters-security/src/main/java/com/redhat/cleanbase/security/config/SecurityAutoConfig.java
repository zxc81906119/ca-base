package com.redhat.cleanbase.security.config;

import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.config.SecurityFlowConfigurer;
import com.redhat.cleanbase.security.flow.form_login.config.FormLoginSecurityFlowConfigurer;
import com.redhat.cleanbase.security.flow.config.getter.SecurityFlowConfigurerGetter;
import com.redhat.cleanbase.security.flow.jwt.config.JwtSecurityFlowConfigurer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityConfigProperties.class)
@Configuration
@Import({FormLoginSecurityFlowConfigurer.class, JwtSecurityFlowConfigurer.class})
public class SecurityAutoConfig {

    private final SecurityConfigProperties securityConfigProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> {
            securityConfigProperties.getIgnoreProperties()
                    .config(webSecurity.ignoring());
        };
    }

    @Bean
    public SecurityFlowConfigurerGetter securityFlowConfigurerGetter(List<SecurityFlowConfigurer> securityFlowConfigurers) {
        return new SecurityFlowConfigurerGetter(securityFlowConfigurers);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            SecurityFlowConfigurerGetter securityFlowConfigurerGetter
    ) throws Exception {
        flowSecurityConfig(httpSecurity, securityFlowConfigurerGetter);
        commonSecurityConfig(httpSecurity);
        return httpSecurity.build();
    }

    private void flowSecurityConfig(HttpSecurity httpSecurity, SecurityFlowConfigurerGetter securityFlowConfigurerGetter) throws Exception {
        val securityType = securityConfigProperties.getSecurityFlowType();
        val securityFlowConfigurers = securityFlowConfigurerGetter.getConditions(securityType);
        if (CollectionUtils.isEmpty(securityFlowConfigurers)) {
            log.warn("未找到 security 驗證流程: {} 對應設定類 !!!", securityType);
            return;
        }

        for (SecurityFlowConfigurer securityFlowConfigurer : securityFlowConfigurers) {
            securityFlowConfigurer.validateProperties(securityConfigProperties);
            securityFlowConfigurer.config(httpSecurity, securityConfigProperties);
        }
    }

    private void commonSecurityConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) -> {
                    securityConfigProperties.getAuthorizeRequestProperties()
                            .config(authorizationManagerRequestMatcherRegistry);
                })
                .cors((corsConfigurer) -> {
                    securityConfigProperties.getCorsProperties()
                            .config(corsConfigurer);
                })
                .headers((headersConfigurer) -> {
                    securityConfigProperties.getHeaderProperties()
                            .config(headersConfigurer);
                })
                .logout((logoutConfigurer) -> {
                    logoutConfigurer
                            .invalidateHttpSession(true)
                            .clearAuthentication(true);

                    securityConfigProperties.getLogoutProperties()
                            .config(logoutConfigurer);
                })
                .sessionManagement((securitySessionManagementConfigurer) -> {
                    securityConfigProperties.getSessionProperties()
                            .config(securitySessionManagementConfigurer);
                });
    }
}
