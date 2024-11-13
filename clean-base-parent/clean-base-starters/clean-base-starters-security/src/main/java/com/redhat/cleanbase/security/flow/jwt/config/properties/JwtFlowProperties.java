package com.redhat.cleanbase.security.flow.jwt.config.properties;

import com.redhat.cleanbase.security.constants.SecurityConstants;
import com.redhat.cleanbase.security.flow.jwt.key.model.impl.DefaultKeyInfo;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(value = SecurityConstants.JWT_PROP_PREFIX)
@Data
public class JwtFlowProperties {

    private String issuer;
    private String loginUri = "/login";

    private boolean postOnly = true;

    private Boolean isTokenInRsHeader = true;

    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private Map<SignatureAlgorithm, Set<DefaultKeyInfo>> signAlgKeyInfo = new HashMap<>();

    private CacheProperties cacheProperties = new CacheProperties();

    private AccessTokenProperties accessTokenProperties = new AccessTokenProperties();

    private RefreshTokenProperties refreshTokenProperties = new RefreshTokenProperties();

    private TimeProperties renewRefreshTokenTimeUntilExpirationInterval = accessTokenProperties.getTimeout();

    @Data
    public static class AccessTokenProperties {
        private String tokenType = "Bearer";
        private String rsParamName = "access-token";
        private String expirationRsParamName = rsParamName + "-expiration";
        private String rqHeaderName = "Authorization";

        private TimeProperties timeout = new TimeProperties(3L, TimeUnit.MINUTES);
    }

    @Data
    public static class RefreshTokenProperties {
        public static final String REFRESH_TOKEN = "refresh-token";

        private Boolean isRenew;
        private String uri = "/token/refresh";
        private String rsParamName = REFRESH_TOKEN;
        private String rqParamName = REFRESH_TOKEN;
        private String expirationRsParamName = rsParamName + "-expiration";
        private TimeProperties timeout = new TimeProperties(10L, TimeUnit.MINUTES);
    }

    @Data
    public static class CacheProperties {
        private TimeProperties maxTimeout = new TimeProperties(30L, TimeUnit.MINUTES);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeProperties {
        private Long time;
        private TimeUnit timeUnit;
    }
}

