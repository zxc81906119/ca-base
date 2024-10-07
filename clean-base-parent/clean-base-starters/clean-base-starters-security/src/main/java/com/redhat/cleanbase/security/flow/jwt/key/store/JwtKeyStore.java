package com.redhat.cleanbase.security.flow.jwt.key.store;

import com.redhat.cleanbase.security.flow.jwt.key.model.KeyInfo;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

public interface JwtKeyStore<I extends KeyInfo> {

    I find(SignatureAlgorithm signatureAlgorithm, String kid);

    default I findRandom(SignatureAlgorithm signatureAlgorithm) {
        val keyInfos = findAll(signatureAlgorithm);
        if (CollectionUtils.isEmpty(keyInfos)) {
            return null;
        }
        return keyInfos.stream()
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    Set<I> findAll(SignatureAlgorithm signatureAlgorithm);
}
