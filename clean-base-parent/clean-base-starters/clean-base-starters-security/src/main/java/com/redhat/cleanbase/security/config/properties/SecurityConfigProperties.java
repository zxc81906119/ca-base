package com.redhat.cleanbase.security.config.properties;

import com.redhat.cleanbase.security.constants.SecurityConstants;
import com.redhat.cleanbase.web.servlet.utils.WebUtils;
import com.redhat.cleanbase.security.flow.SecurityFlowType;
import jakarta.servlet.DispatcherType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@ConfigurationProperties(SecurityConstants.SECURITY_PROP_PREFIX)
public class SecurityConfigProperties {

    private SecurityFlowType securityFlowType;

    private CorsProperties corsProperties = new CorsProperties();

    private LogoutProperties logoutProperties = new LogoutProperties();

    private IgnoreProperties ignoreProperties = new IgnoreProperties();

    private HeaderProperties headerProperties = new HeaderProperties();

    private SessionProperties sessionProperties = new SessionProperties();

    private AuthorizeRequestProperties authorizeRequestProperties = new AuthorizeRequestProperties();

    private CsrfProperties csrfProperties = new CsrfProperties();

    @Data
    public static class SessionProperties implements ConfigurableProperties<SessionManagementConfigurer<?>> {
        private SessionCreationPolicy createPolicy;

        public void config(SessionManagementConfigurer<?> sessionManagementConfigurer) {
            if (createPolicy != null) {
                sessionManagementConfigurer.sessionCreationPolicy(createPolicy);
            }
        }
    }

    @Data
    public static class LogoutProperties implements ConfigurableProperties<LogoutConfigurer<?>> {
        private String url = "/logout";

        public void config(LogoutConfigurer<?> logoutConfigurer) {
            if (url != null && !url.isBlank()) {
                logoutConfigurer.logoutUrl(url.trim());
            }
        }
    }

    @Data
    public static class HeaderProperties implements ConfigurableProperties<HeadersConfigurer<?>> {
        private CSPProperties cspProperties = new CSPProperties();
        private FrameProperties frameProperties;

        @Override
        public void config(HeadersConfigurer<?> headersConfigurer) {
            headersConfigurer.contentSecurityPolicy((config) ->
                    cspProperties.config(config)
            );

            if (frameProperties != null) {
                headersConfigurer.frameOptions((config) ->
                        frameProperties.config(config)
                );
            }
        }

        public enum FrameProperties implements ConfigurableProperties<HeadersConfigurer<?>.FrameOptionsConfig> {
            DENY {
                @Override
                public void config(HeadersConfigurer<?>.FrameOptionsConfig frameOptionsConfig) {
                    frameOptionsConfig.deny();
                }
            },
            SAME_ORIGIN {
                @Override
                public void config(HeadersConfigurer<?>.FrameOptionsConfig frameOptionsConfig) {
                    frameOptionsConfig.sameOrigin();
                }
            },
            DISABLE {
                @Override
                public void config(HeadersConfigurer<?>.FrameOptionsConfig frameOptionsConfig) {
                    frameOptionsConfig.disable();
                }
            }
        }

        @Data
        public static class CSPProperties implements ConfigurableProperties<HeadersConfigurer<?>.ContentSecurityPolicyConfig> {
            // TODO 整理格式,變成 enum 供使用
            //  整理完就要用 list 去裝
            //  目前就是直接無腦放值即可
            private String content;

            @Override
            public void config(HeadersConfigurer<?>.ContentSecurityPolicyConfig config) {
                if (content != null && !content.isBlank()) {
                    config.policyDirectives(content.trim());
                }
            }
        }
    }

    @Data
    public static class IgnoreProperties implements ConfigurableProperties<WebSecurity.IgnoredRequestConfigurer> {

        private Set<RequestMatchProperties> requestMatchPropertiesSet;

        @Override
        public void config(WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer) {
            if (!CollectionUtils.isEmpty(requestMatchPropertiesSet)) {
                for (RequestMatchProperties requestMatchProperties : requestMatchPropertiesSet) {
                    val matchRequestConfig = requestMatchProperties.matchRequest(ignoredRequestConfigurer);
                    if (matchRequestConfig != null) {
                        ignoredRequestConfigurer = matchRequestConfig;
                    }
                }
            }
        }
    }

    @Data
    public static class CorsProperties implements ConfigurableProperties<CorsConfigurer<?>> {
        private Set<String> allowOrigins;
        private Set<String> acceptHttpHeaders;
        private Set<String> explodeHttpHeaders;
        private Set<RequestMethod> acceptHttpMethods;
        private Boolean allowAllHeaders = false;
        private Boolean withCredentials = false;
        private Boolean acceptAllOrigin = false;
        private Boolean explodeAllHeaders = false;
        private Boolean allowAllHttpMethods = false;

        @Override
        public void config(CorsConfigurer<?> corsConfigurer) {
            corsConfigurer.configurationSource((request) -> {
                val corsConfiguration = new CorsConfiguration();

                if (withCredentials != null) {
                    corsConfiguration.setAllowCredentials(withCredentials);
                }

                if (Boolean.TRUE.equals(allowAllHttpMethods)) {
                    val method = request.getMethod();
                    corsConfiguration.setAllowedMethods(List.of(method));
                } else {
                    if (!CollectionUtils.isEmpty(acceptHttpMethods)) {
                        val httpMethodNames = acceptHttpMethods.stream()
                                .map(RequestMethod::asHttpMethod)
                                .map(HttpMethod::name)
                                .collect(Collectors.toList());
                        corsConfiguration.setAllowedMethods(httpMethodNames);
                    }
                }

                val responseOptional = WebUtils.getHttpServletResponse();
                if (Boolean.TRUE.equals(explodeAllHeaders) && responseOptional.isPresent()) {
                    val headerNames = responseOptional.get().getHeaderNames();
                    if (!CollectionUtils.isEmpty(headerNames)) {
                        corsConfiguration.setExposedHeaders(headerNames.stream().toList());
                    }
                } else {
                    if (!CollectionUtils.isEmpty(explodeHttpHeaders)) {
                        corsConfiguration.setExposedHeaders(explodeHttpHeaders.stream().toList());
                    }
                }

                if (Boolean.TRUE.equals(allowAllHeaders)) {
                    val headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            val headerName = headerNames.nextElement();
                            corsConfiguration.addAllowedHeader(headerName);
                        }
                    }
                } else {
                    if (!CollectionUtils.isEmpty(acceptHttpHeaders)) {
                        corsConfiguration.setAllowedHeaders(acceptHttpHeaders.stream().toList());
                    }
                }

                if (Boolean.TRUE.equals(acceptAllOrigin)) {
                    val origin = request.getHeader("Origin");
                    if (origin != null) {
                        corsConfiguration.setAllowedOrigins(List.of(origin));
                    } else {
                        if (Boolean.TRUE.equals(withCredentials)) {
                            if (!CollectionUtils.isEmpty(allowOrigins)) {
                                corsConfiguration.setAllowedOrigins(
                                        allowOrigins.stream().toList()
                                );
                            }
                        } else {
                            corsConfiguration.setAllowedOrigins(List.of("*"));
                        }
                    }
                } else {
                    if (!CollectionUtils.isEmpty(allowOrigins)) {
                        corsConfiguration.setAllowedOrigins(
                                allowOrigins.stream().toList()
                        );
                    }
                }

                return corsConfiguration;
            });
        }
    }

    @Data
    public static class AuthorizeRequestProperties implements ConfigurableProperties<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {
        private List<RequestMatchAuthProperties> requestMatchAuthPropertiesList;

        @Override
        public void config(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
            if (!CollectionUtils.isEmpty(requestMatchAuthPropertiesList)) {
                for (RequestMatchAuthProperties requestMatchAuthProperties : requestMatchAuthPropertiesList) {
                    registry = requestMatchAuthProperties.matchRequestAndConfigAuth(registry);
                }
            }
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        public static class RequestMatchAuthProperties extends RequestMatchProperties {
            private Boolean denyAll;
            private Boolean authenticated;
            private String hasRole;
            private String hasAuthority;
            private Set<String> hasAnyRole;
            private Set<String> hasAnyAuthority;
            private Boolean permitAll;

            public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry matchRequestAndConfigAuth(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                val authorizedUrl = matchRequest(registry);
                if (authorizedUrl == null) {
                    return registry;
                }

                if (Boolean.TRUE.equals(denyAll)) {
                    return authorizedUrl.denyAll();
                }

                if (Boolean.TRUE.equals(authenticated)) {
                    return authorizedUrl.authenticated();
                }

                if (hasRole != null && !hasRole.isBlank()) {
                    return authorizedUrl.hasRole(hasRole.trim());
                }

                if (hasAuthority != null && !hasAuthority.isBlank()) {
                    return authorizedUrl.hasRole(hasAuthority.trim());
                }

                if (!CollectionUtils.isEmpty(hasAnyRole)) {
                    return authorizedUrl.hasAnyRole(hasAnyRole.toArray(String[]::new));
                }

                if (!CollectionUtils.isEmpty(hasAnyAuthority)) {
                    return authorizedUrl.hasAnyAuthority(hasAnyAuthority.toArray(String[]::new));
                }

                if (Boolean.TRUE.equals(permitAll)) {
                    return authorizedUrl.permitAll();
                }

                return registry;
            }
        }

    }

    @Data
    public static class RequestMatchProperties {
        private Set<String> urls;
        private Boolean anyRequest = false;
        private RequestMethod requestMethod;
        private Set<DispatcherType> dispatcherTypes;

        public <T> T matchRequest(AbstractRequestMatcherRegistry<T> abstractRequestMatcherRegistry) {
            if (Boolean.TRUE.equals(anyRequest)) {
                return abstractRequestMatcherRegistry.anyRequest();
            }
            if (CollectionUtils.isEmpty(dispatcherTypes)) {
                if (requestMethod != null) {
                    if (CollectionUtils.isEmpty(urls)) {
                        return abstractRequestMatcherRegistry.requestMatchers(requestMethod.asHttpMethod());
                    } else {
                        return abstractRequestMatcherRegistry.requestMatchers(requestMethod.asHttpMethod(), urls.toArray(String[]::new));
                    }
                } else {
                    if (!CollectionUtils.isEmpty(urls)) {
                        return abstractRequestMatcherRegistry.requestMatchers(urls.toArray(String[]::new));
                    } else {
                        return null;
                    }
                }
            } else {
                if (requestMethod != null) {
                    return abstractRequestMatcherRegistry.dispatcherTypeMatchers(requestMethod.asHttpMethod(), dispatcherTypes.toArray(DispatcherType[]::new));
                } else {
                    return abstractRequestMatcherRegistry.dispatcherTypeMatchers(dispatcherTypes.toArray(DispatcherType[]::new));
                }
            }

        }
    }

    @Data
    public static class CsrfProperties implements ConfigurableProperties<CsrfConfigurer<?>> {
        private Set<String> ignoreUrls;

        @Override
        public void config(CsrfConfigurer<?> csrfConfigurer) {
            if (!CollectionUtils.isEmpty(ignoreUrls)) {
                csrfConfigurer.ignoringRequestMatchers(ignoreUrls.toArray(String[]::new));
            }
        }


    }


}