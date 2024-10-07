package com.redhat.cleanbase.security.flow.jwt.config.properties;

import com.redhat.cleanbase.security.config.properties.TimeProperties;
import com.redhat.cleanbase.security.flow.jwt.key.model.impl.DefaultKeyInfo;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
        private String rqHeaderName = "Authorization";

        private TimeProperties timeout = new TimeProperties(3L, TimeUnit.MINUTES);
    }

    @Data
    public static class RefreshTokenProperties {
        private Boolean isRenew;

        private String uri = "/token/refresh";
        private String rsParamName = "refresh-token";

        private TimeProperties timeout = new TimeProperties(10L, TimeUnit.MINUTES);
    }

    @Data
    public static class CacheProperties {
        private TimeProperties maxTimeout = new TimeProperties(30L, TimeUnit.MINUTES);
    }
}

