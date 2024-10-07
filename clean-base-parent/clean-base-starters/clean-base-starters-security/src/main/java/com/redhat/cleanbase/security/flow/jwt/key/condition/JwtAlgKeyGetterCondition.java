package com.redhat.cleanbase.security.flow.jwt.key.condition;

import com.redhat.cleanbase.common.type.Condition;
import com.redhat.cleanbase.security.flow.jwt.key.getter.JwtAlgKeyGetter;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public interface JwtAlgKeyGetterCondition extends Condition<SignatureAlgorithm>, JwtAlgKeyGetter {
    default boolean isSupported(SignatureAlgorithm signatureAlgorithm) {
        if (signatureAlgorithm == null) {
            return false;
        }
        val supportedSignatureAlgorithms = getSupportedSignatureAlgorithms();
        if (CollectionUtils.isEmpty(supportedSignatureAlgorithms)) {
            return false;
        }
        return supportedSignatureAlgorithms.contains(signatureAlgorithm);
    }

    Set<SignatureAlgorithm> getSupportedSignatureAlgorithms();

}
