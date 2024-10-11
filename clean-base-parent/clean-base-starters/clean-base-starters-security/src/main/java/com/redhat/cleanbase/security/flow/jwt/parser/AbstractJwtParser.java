package com.redhat.cleanbase.security.flow.jwt.parser;

import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.security.flow.jwt.exception.JwtParseAuthenticationException;
import com.redhat.cleanbase.security.flow.jwt.key.getter.impl.DelegateJwtKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.key.model.KeyWithId;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public abstract class AbstractJwtParser<T extends JwtToken> implements JwtParser<T> {

    private final DelegateJwtKeyGetter delegateJwtKeyGetter;

    private static Jws<Claims> getClaimsJws(String jwt, KeyWithId keyWithId) {
        val jwtParserBuilder = Jwts.parserBuilder();
        if (keyWithId != null) {
            jwtParserBuilder.setSigningKey(keyWithId.getKey());
        }
        return jwtParserBuilder.build()
                .parseClaimsJws(jwt);
    }

    private static String getNoSignString(String jwt) throws JwtParseAuthenticationException {
        val i = jwt.lastIndexOf('.');
        if (i == -1) {
            throw new JwtParseAuthenticationException("jwt 必須存在'.'字元");
        }
        return jwt.substring(0, i + 1);
    }

    private static Header<?> getJwtHeader(String jwt) {
        return Jwts.parserBuilder().build()
                .parseClaimsJwt(jwt)
                .getHeader();
    }

    protected abstract T createToken();

    @Override
    public T parse(@NonNull String jwt) throws JwtParseAuthenticationException {
        val keyWithId = getKeyWithId(jwt);

        val claimsJws = getClaimsJws(jwt, keyWithId);

        val body = claimsJws.getBody();
        val jwtToken = createToken();
        jwtToken.setId(body.getId());
        jwtToken.setSubject(body.getSubject());
        jwtToken.setPayload(body);
        jwtToken.setCreationTime(body.getIssuedAt());
        jwtToken.setExpireTime(body.getExpiration());
        jwtToken.setTokenString(jwt);

        return jwtToken;
    }

    private KeyWithId getKeyWithId(String jwt) throws JwtParseAuthenticationException {
        val noSignString = getNoSignString(jwt);
        val header = getJwtHeader(noSignString);
        return getKey(header);
    }

    private KeyWithId getKey(Header<?> header) {
        val keyId = CastUtils.cast(header.get(JwsHeader.KEY_ID), String.class);
        val algorithm = CastUtils.cast(header.get(JwsHeader.ALGORITHM), String.class);
        if (keyId == null || algorithm == null) {
            return null;
        }
        val signatureAlgorithm = SignatureAlgorithm.forName(algorithm);
        return delegateJwtKeyGetter.getVerifyKey(signatureAlgorithm, keyId);
    }
}