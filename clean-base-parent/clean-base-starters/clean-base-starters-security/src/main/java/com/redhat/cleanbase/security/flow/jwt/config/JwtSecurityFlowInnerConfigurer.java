package com.redhat.cleanbase.security.flow.jwt.config;

import com.redhat.cleanbase.common.lock.ResourceLock;
import com.redhat.cleanbase.common.lock.impl.DefaultResourceLock;
import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.accessor.impl.DefaultAuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.impl.DefaultJwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import com.redhat.cleanbase.security.flow.jwt.converter.impl.DefaultRequestDtoConverter;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultAccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.exception.handler.AccessDeniedExceptionHandler;
import com.redhat.cleanbase.security.flow.jwt.exception.handler.AuthenticationExceptionHandler;
import com.redhat.cleanbase.security.flow.jwt.filter.AccessTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.BaseLoginFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.RefreshTokenFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.RefreshTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.entrypoint.DefaultAuthenticationEntryPoint;
import com.redhat.cleanbase.security.flow.jwt.filter.handler.impl.*;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultAccessTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultLoginFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultRefreshTokenFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultRefreshTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.DefaultLoginRequestDto;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import com.redhat.cleanbase.security.flow.jwt.filter.provider.LoginProvider;
import com.redhat.cleanbase.security.flow.jwt.filter.provider.impl.DefaultLoginProvider;
import com.redhat.cleanbase.security.flow.jwt.filter.service.LoginAuthTokenUserDetailsService;
import com.redhat.cleanbase.security.flow.jwt.filter.service.impl.DefaultLoginAuthTokenUserDetailsService;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.AbstractRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.impl.DefaultAccessTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.generator.impl.DefaultRefreshTokenGenerator;
import com.redhat.cleanbase.security.flow.jwt.key.condition.JwtAlgKeyGetterCondition;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.HSJwtAlgKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.key.model.impl.DefaultKeyInfo;
import com.redhat.cleanbase.security.flow.jwt.key.store.JwtKeyStore;
import com.redhat.cleanbase.security.flow.jwt.key.store.impl.PropertiesJwtKeyStore;
import com.redhat.cleanbase.security.flow.jwt.parser.AccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.RefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.impl.DefaultAccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.impl.DefaultRefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqAccessTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.getter.RqRefreshTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.getter.impl.DefaultRqAccessTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.getter.impl.DefaultRqRefreshTokenGetter;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.writer.TokenRsWriter;
import com.redhat.cleanbase.security.flow.jwt.token.writer.impl.DefaultTokenRsWriter;
import com.redhat.cleanbase.security.flow.jwt.validator.AccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.RefreshTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.impl.DefaultAccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.impl.DefaultRefreshTokenValidator;
import com.redhat.cleanbase.validation.GenericValidator;
import com.redhat.cleanbase.web.model.request.GenericRequest;
import com.redhat.cleanbase.web.servlet.exception.handler.RqDelegatingExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.RsEntityRsWriter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class JwtSecurityFlowInnerConfigurer {

    private final JwtFlowProperties jwtFlowProperties;

    // todo 原生
    @ConditionalOnMissingBean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @ConditionalOnMissingBean
    @Bean
    public LogoutHandler logoutHandler() {
        return new DefaultLogoutHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DefaultLogoutSuccessHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessDeniedHandler accessDeniedHandler(RqDelegatingExceptionHandler rqDelegatingExceptionHandler) {
        return new DefaultAccessDeniedHandler(rqDelegatingExceptionHandler);
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(RqDelegatingExceptionHandler rqDelegatingExceptionHandler) {
        return new DefaultAuthenticationEntryPoint(rqDelegatingExceptionHandler);
    }

    // todo 客製

    @Bean
    public DelegateJwtKeyGetter keyGetterProxy(List<JwtAlgKeyGetterCondition> jwtAlgKeyGetterConditions) {
        return new DelegateJwtKeyGetter(jwtAlgKeyGetterConditions);
    }

    @ConditionalOnMissingBean
    @Bean
    public BaseLoginFilter<?> loginFilter(
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            RequestDtoConverter<GenericRequest<DefaultLoginRequestDto>> requestDtoConverter
    ) {
        val loginFilter = new DefaultLoginFilter(requestDtoConverter);
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setFilterProcessesUrl(jwtFlowProperties.getLoginUri());
        loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        loginFilter.setPostOnly(jwtFlowProperties.isPostOnly());
        return loginFilter;
    }

    @ConditionalOnMissingBean
    @Bean
    public RequestDtoConverter<?> requestTransfer(JacksonJsonParser<?> jacksonJsonParser) {
        return new DefaultRequestDtoConverter(jacksonJsonParser);
    }

    @ConditionalOnMissingBean
    @Bean
    public LoginProvider<?, ?> loginProvider(
            PasswordEncoder passwordEncoder,
            AuthenticationAccessor<User> authenticationAccessor,
            LoginAuthTokenUserDetailsService<LoginAuthToken, User> loginAuthTokenUserDetailsService
    ) {
        return new DefaultLoginProvider(passwordEncoder, authenticationAccessor, loginAuthTokenUserDetailsService);
    }

    @ConditionalOnMissingBean
    @Bean
    public LoginAuthTokenUserDetailsService<?, ?> loginAuthTokenUserDetailsService() {
        return new DefaultLoginAuthTokenUserDetailsService();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(RqDelegatingExceptionHandler rqDelegatingExceptionHandler) {
        return new DefaultAuthenticationFailureHandler(rqDelegatingExceptionHandler);
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationExceptionHandler actionExceptionHandler() {
        return new AuthenticationExceptionHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessDeniedExceptionHandler accessDeniedExceptionHandler() {
        return new AccessDeniedExceptionHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(
            TokenRsWriter tokenRsWriter,
            JwtCacheManager<?> jwtCacheManager,
            AuthenticationAccessor<User> authenticationAccessor,
            AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator,
            AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator
    ) {
        return new DefaultJwtAuthenticationSuccessHandler(tokenRsWriter, jwtFlowProperties, jwtCacheManager, authenticationAccessor, accessTokenGenerator, refreshTokenGenerator);
    }

    @ConditionalOnMissingBean
    @Bean
    public AbstractAccessTokenGenerator<?, ?> accessTokenGenerator(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        return new DefaultAccessTokenGenerator(jwtFlowProperties, delegateJwtKeyGetter);
    }

    @ConditionalOnMissingBean
    @Bean
    public AbstractRefreshTokenGenerator<?, ?> refreshTokenGenerator(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        return new DefaultRefreshTokenGenerator(jwtFlowProperties, delegateJwtKeyGetter);
    }

    @ConditionalOnMissingBean
    @Bean
    public HSJwtAlgKeyGetter hs256KeyGetter(JwtKeyStore<DefaultKeyInfo> jwtKeyStore) {
        return new HSJwtAlgKeyGetter(jwtKeyStore);
    }

    @ConditionalOnMissingBean
    @Bean
    public JwtKeyStore<?> jwtKeyStore() {
        return new PropertiesJwtKeyStore(jwtFlowProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public JwtCacheManager<?> jwtCacheManager() {
        return new DefaultJwtCacheManager();
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessTokenVerifyFilter<?> accessTokenVerifyFilter(
            AccessTokenValidator<DefaultAccessToken> jwtValidator, AccessTokenParser<DefaultAccessToken> jwtParser, JwtCacheManager<?> jwtCacheManager, RqAccessTokenGetter rqAccessTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegatingExceptionHandler rqDelegatingExceptionHandler
    ) {
        return new DefaultAccessTokenVerifyFilter(jwtFlowProperties, jwtValidator, jwtParser, jwtCacheManager, rqAccessTokenGetter, authenticationAccessor, rqDelegatingExceptionHandler);
    }

    @ConditionalOnMissingBean
    @Bean
    public RqRefreshTokenGetter rqRefreshTokenGetter() {
        return new DefaultRqRefreshTokenGetter(jwtFlowProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public RqAccessTokenGetter rqAccessTokenGetter() {
        return new DefaultRqAccessTokenGetter(jwtFlowProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public RefreshTokenVerifyFilter<?> refreshTokenVerifyFilter(
            RefreshTokenValidator<DefaultRefreshToken> jwtValidator, RefreshTokenParser<DefaultRefreshToken> jwtParser, JwtCacheManager<?> jwtCacheManager, RqRefreshTokenGetter rqRefreshTokenGetter, AuthenticationAccessor<?> authenticationAccessor, RqDelegatingExceptionHandler rqDelegatingExceptionHandler, SecurityConfigProperties securityConfigProperties
    ) {
        return new DefaultRefreshTokenVerifyFilter(jwtFlowProperties, jwtValidator, jwtParser, jwtCacheManager, rqRefreshTokenGetter, authenticationAccessor, rqDelegatingExceptionHandler, securityConfigProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessTokenParser<?> accessTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        return new DefaultAccessTokenParser(delegateJwtKeyGetter);
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessTokenValidator<?> accessTokenValidator(GenericValidator genericValidator) {
        return new DefaultAccessTokenValidator(genericValidator);
    }

    @ConditionalOnMissingBean
    @Bean
    public ResourceLock resourceLock() {
        return new DefaultResourceLock();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationAccessor<?> authenticationAccessor() {
        return new DefaultAuthenticationAccessor();
    }

    @ConditionalOnMissingBean
    @Bean
    public TokenRsWriter tokenRsWriter(RsEntityRsWriter rsEntityRsWriter) {
        return new DefaultTokenRsWriter(jwtFlowProperties, rsEntityRsWriter);
    }

    @ConditionalOnMissingBean
    @Bean
    public RefreshTokenFilter<?, ?, ?, ?> refreshTokenFilter(
            ResourceLock resourceLock, TokenRsWriter tokenRsWriter, JwtCacheManager<?> jwtCacheManager, RqRefreshTokenGetter rqRefreshTokenGetter, RefreshTokenParser<DefaultRefreshToken> refreshTokenParser, RefreshTokenValidator<DefaultRefreshToken> refreshTokenValidator, RqDelegatingExceptionHandler rqDelegatingExceptionHandler, AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator, AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator
    ) {
        return new DefaultRefreshTokenFilter(jwtFlowProperties, resourceLock, tokenRsWriter, jwtCacheManager, rqRefreshTokenGetter, refreshTokenParser, refreshTokenValidator, rqDelegatingExceptionHandler, accessTokenGenerator, refreshTokenGenerator);
    }

    @ConditionalOnMissingBean
    @Bean
    public RefreshTokenParser<?> refreshTokenParser(DelegateJwtKeyGetter delegateJwtKeyGetter) {
        return new DefaultRefreshTokenParser(delegateJwtKeyGetter);
    }

    @ConditionalOnMissingBean
    @Bean
    public RefreshTokenValidator<?> refreshTokenValidator(GenericValidator genericValidator) {
        return new DefaultRefreshTokenValidator(genericValidator);
    }


}
