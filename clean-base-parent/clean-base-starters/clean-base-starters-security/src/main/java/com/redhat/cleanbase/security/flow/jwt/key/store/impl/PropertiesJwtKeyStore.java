package com.redhat.cleanbase.security.flow.jwt.key.store.impl;

import com.redhat.cleanbase.security.flow.jwt.config.properties.JwtFlowProperties;
import com.redhat.cleanbase.security.flow.jwt.key.model.impl.DefaultKeyInfo;
import com.redhat.cleanbase.security.flow.jwt.key.store.JwtKeyStore;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class PropertiesJwtKeyStore implements JwtKeyStore<DefaultKeyInfo> {

    private final JwtFlowProperties jwtProperties;

    @Override
    public DefaultKeyInfo find(SignatureAlgorithm signatureAlgorithm, @NonNull String kid) {
        val keyInfos =
                jwtProperties.getSignAlgKeyInfo()
                        .get(signatureAlgorithm);
        if (CollectionUtils.isEmpty(keyInfos)) {
            return null;
        }
        return keyInfos.stream()
                .filter(Objects::nonNull)
                .filter((keyInfo) -> kid.equals(keyInfo.getKid()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<DefaultKeyInfo> findAll(SignatureAlgorithm signatureAlgorithm) {
        return jwtProperties.getSignAlgKeyInfo()
                .get(signatureAlgorithm);
    }
}
