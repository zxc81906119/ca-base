package com.redhat.cleanbase.security.flow.jwt.generator;


import com.redhat.cleanbase.security.config.properties.TimeProperties;
import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.datasource.JwtDataSource;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class AbstractJwtTokenGenerator<T extends JwtToken, TS extends JwtDataSource> implements JwtGenerator<T, TS> {

    protected final JwtFlowProperties jwtProperties;

    private final DelegateJwtKeyGetter delegateJwtKeyGetter;

    @Override
    public T generate(TS ts) {
        // 套件
        val jwtBuilder = Jwts.builder();

        val token = createToken();
        // user
        val id = Objects.requireNonNull(getId(ts), "id must exist");
        jwtBuilder.setId(id);
        token.setId(id);

        // group
        val subject = Objects.requireNonNull(getSubject(ts), "subject must exist");
        jwtBuilder.setSubject(subject);
        token.setSubject(subject);

        val now = new Date();
        jwtBuilder.setIssuedAt(now);
        token.setCreationTime(now);

        val signatureAlgorithm = jwtProperties.getSignatureAlgorithm();
        val keyWithId = delegateJwtKeyGetter.getSignKey(signatureAlgorithm);
        if (keyWithId != null) {
            jwtBuilder.signWith(keyWithId.getKey(), signatureAlgorithm);
            jwtBuilder.setHeaderParam(JwsHeader.KEY_ID, keyWithId.getKid());
            jwtBuilder.setHeaderParam(JwsHeader.ALGORITHM, signatureAlgorithm.getValue());
        }

        // customPayload
        val customPayload = getClaims(ts);
        if (customPayload != null) {
            jwtBuilder.addClaims(customPayload);
            token.setPayload(customPayload);
        }

        val timeProperties = getExpireTimeInfo(ts);
        if (timeProperties != null) {
            val time = timeProperties.getTime();
            if (time != null && time >= 0) {
                val timeUnit = Optional.ofNullable(timeProperties.getTimeUnit()).orElse(TimeUnit.MINUTES);
                val expireDate = new Date(now.getTime() + timeUnit.toMillis(time));
                jwtBuilder.setExpiration(expireDate);
                token.setExpireTime(expireDate);
            }
        }

        val issuer = jwtProperties.getIssuer();
        if (issuer != null && !issuer.isBlank()) {
            jwtBuilder.setIssuer(issuer.trim());
        }

        val jwtTokenString = jwtBuilder.compact();
        token.setTokenString(jwtTokenString);

        return token;
    }

    protected abstract TimeProperties getExpireTimeInfo(TS ts);

    protected abstract String getId(TS ts);

    protected abstract T createToken();

    protected abstract String getSubject(TS ts);

    protected Map<String, Object> getClaims(TS ts) {
        return null;
    }
}