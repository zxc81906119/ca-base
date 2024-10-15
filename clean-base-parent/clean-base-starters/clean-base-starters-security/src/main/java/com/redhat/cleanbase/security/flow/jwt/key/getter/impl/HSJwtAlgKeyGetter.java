package com.redhat.cleanbase.security.flow.jwt.key.getter.impl;

import com.redhat.cleanbase.security.flow.jwt.key.model.impl.DefaultKeyInfo;
import com.redhat.cleanbase.security.flow.jwt.key.condition.AbstractJwtAlgKeyGetterCondition;
import com.redhat.cleanbase.security.flow.jwt.key.store.JwtKeyStore;
import com.redhat.cleanbase.security.flow.jwt.key.model.KeyWithId;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.val;

import java.util.Base64;
import java.util.Set;

public class HSJwtAlgKeyGetter extends AbstractJwtAlgKeyGetterCondition<DefaultKeyInfo> {

    public HSJwtAlgKeyGetter(JwtKeyStore<DefaultKeyInfo> jwtKeyStore) {
        super(jwtKeyStore);
    }

    @Override
    public Set<SignatureAlgorithm> getSupportedSignatureAlgorithms() {
        return Set.of(
                SignatureAlgorithm.HS256,
                SignatureAlgorithm.HS384,
                SignatureAlgorithm.HS512
        );
    }

    @Override
    protected KeyWithId getVerifyKey(DefaultKeyInfo keyInfo) {
        return getSignKey(keyInfo);
    }

    @Override
    public KeyWithId getSignKey(DefaultKeyInfo keyInfo) {
        if (keyInfo == null) {
            return null;
        }

        val kid = keyInfo.getKid();
        val privateKey = keyInfo.getBase64PrivateKey();

        if (kid == null || privateKey == null) {
            return null;
        }

        return KeyWithId.builder()
                .kid(kid)
                .key(Keys.hmacShaKeyFor(
                        Base64.getDecoder().decode(privateKey)
                ))
                .build();
    }


}
