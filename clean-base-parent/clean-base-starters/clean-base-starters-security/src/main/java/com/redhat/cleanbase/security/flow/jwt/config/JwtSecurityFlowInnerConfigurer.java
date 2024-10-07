package com.redhat.cleanbase.security.flow.jwt.config;

import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.security.config.properties.SecurityConfigProperties;
import com.redhat.cleanbase.security.flow.jwt.token.writer.impl.DefaultRsTokenWriter;
import com.redhat.cleanbase.security.flow.jwt.accessor.AuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.accessor.impl.DefaultAuthenticationAccessor;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.JwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.cache.manager.impl.DefaultJwtCacheManager;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestConverter;
import com.redhat.cleanbase.security.flow.jwt.converter.impl.DefaultRequestConverter;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultAccessTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.exception.handler.ActionExceptionHandler;
import com.redhat.cleanbase.security.flow.jwt.filter.*;
import com.redhat.cleanbase.security.flow.jwt.filter.handler.impl.DefaultAuthenticationFailureHandler;
import com.redhat.cleanbase.security.flow.jwt.filter.handler.impl.DefaultJwtAuthenticationSuccessHandler;
import com.redhat.cleanbase.security.flow.jwt.filter.handler.impl.DefaultLogoutHandler;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultAccessTokenVerifyFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultLoginFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.impl.DefaultRefreshTokenFilter;
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
import com.redhat.cleanbase.security.flow.jwt.lock.ResourceLock;
import com.redhat.cleanbase.security.flow.jwt.lock.impl.DefaultResourceLock;
import com.redhat.cleanbase.security.flow.jwt.parser.AccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.RefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.impl.DefaultAccessTokenParser;
import com.redhat.cleanbase.security.flow.jwt.parser.impl.DefaultRefreshTokenParser;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultAccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.impl.DefaultRefreshToken;
import com.redhat.cleanbase.security.flow.jwt.token.writer.RsTokenWriter;
import com.redhat.cleanbase.security.flow.jwt.validator.AccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.RefreshTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.impl.DefaultAccessTokenValidator;
import com.redhat.cleanbase.security.flow.jwt.validator.impl.DefaultRefreshTokenValidator;
import com.redhat.cleanbase.validation.GenericValidator;
import com.redhat.cleanbase.web.model.request.GenericRequest;
import com.redhat.cleanbase.web.servlet.exception.handler.impl.RqDelegateExceptionHandler;
import com.redhat.cleanbase.web.servlet.response.processor.WriteRsEntityToRsProcessor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.List;


@Configuration
public class JwtSecurityFlowInnerConfigurer {

    private final JwtFlowProperties jwtFlowProperties;

    public JwtSecurityFlowInnerConfigurer(SecurityConfigProperties securityConfigProperties) {
        this.jwtFlowProperties = securityConfigProperties.getJwtFlowProperties();
    }

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

    // todo 客製

    @Bean
    public DelegateJwtKeyGetter keyGetterProxy(List<JwtAlgKeyGetterCondition> keyGetterConditions) {
        return new DelegateJwtKeyGetter(keyGetterConditions);
    }


    @ConditionalOnMissingBean
    @Bean
    public BaseLoginFilter<?> loginFilter(
            AuthenticationManager authenticationManager,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationFailureHandler authenticationFailureHandler,
            RequestConverter<GenericRequest<DefaultLoginRequestDto>> requestConverter
    ) {
        val loginFilter = new DefaultLoginFilter(requestConverter);
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setFilterProcessesUrl(jwtFlowProperties.getLoginUri());
        loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        loginFilter.setPostOnly(jwtFlowProperties.isPostOnly());
        return loginFilter;
    }

    @ConditionalOnMissingBean
    @Bean
    public RequestConverter<?> requestTransfer(JacksonJsonParser<?> jacksonJsonParser) {
        return new DefaultRequestConverter(jacksonJsonParser);
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
    public AuthenticationFailureHandler authenticationFailureHandler(RqDelegateExceptionHandler rqDelegateExceptionHandler) {
        return new DefaultAuthenticationFailureHandler(rqDelegateExceptionHandler);
    }

    @ConditionalOnMissingBean
    @Bean
    public ActionExceptionHandler actionExceptionHandler() {
        return new ActionExceptionHandler();
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(
            RsTokenWriter rsTokenWriter,
            JwtCacheManager jwtCacheManager,
            AuthenticationAccessor<User> authenticationAccessor,
            AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator,
            AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator
    ) {
        return new DefaultJwtAuthenticationSuccessHandler(rsTokenWriter, jwtFlowProperties, jwtCacheManager, authenticationAccessor, accessTokenGenerator, refreshTokenGenerator);
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
    public JwtCacheManager jwtCacheManager() {
        return new DefaultJwtCacheManager();
    }

    @ConditionalOnMissingBean
    @Bean
    public AccessTokenVerifyFilter<?> jwtVerifyFilter(
            JwtCacheManager jwtCacheManager,
            AuthenticationAccessor<User> authenticationAccessor,
            RqDelegateExceptionHandler rqDelegateExceptionHandler,
            AccessTokenParser<DefaultAccessToken> accessTokenParser,
            AccessTokenValidator<DefaultAccessToken> accessTokenValidator
    ) {
        return new DefaultAccessTokenVerifyFilter(jwtFlowProperties, jwtCacheManager, accessTokenParser, accessTokenValidator, authenticationAccessor, rqDelegateExceptionHandler);
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
    public RsTokenWriter rsTokenWriter(WriteRsEntityToRsProcessor writeRsEntityToRsProcessor) {
        return new DefaultRsTokenWriter(jwtFlowProperties, writeRsEntityToRsProcessor);
    }

    @ConditionalOnMissingBean
    @Bean
    public RefreshTokenFilter<?, ?, ?, ?> refreshTokenFilter(
            ResourceLock resourceLock,
            RsTokenWriter rsTokenWriter,
            JwtCacheManager jwtCacheManager,
            RefreshTokenParser<DefaultRefreshToken> refreshTokenParser,
            RefreshTokenValidator<DefaultRefreshToken> refreshTokenValidator,
            RqDelegateExceptionHandler rqDelegateExceptionHandler,
            AbstractAccessTokenGenerator<DefaultAccessToken, DefaultAccessTokenDataSource> accessTokenGenerator,
            AbstractRefreshTokenGenerator<DefaultRefreshToken, DefaultRefreshTokenDataSource> refreshTokenGenerator
    ) {
        return new DefaultRefreshTokenFilter(resourceLock, rsTokenWriter, jwtCacheManager, jwtFlowProperties, refreshTokenParser, refreshTokenValidator, rqDelegateExceptionHandler, accessTokenGenerator, refreshTokenGenerator);
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
