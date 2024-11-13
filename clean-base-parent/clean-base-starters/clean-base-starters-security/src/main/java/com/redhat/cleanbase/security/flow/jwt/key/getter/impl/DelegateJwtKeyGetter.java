package com.redhat.cleanbase.security.flow.jwt.key.getter.impl;

import com.redhat.cleanbase.security.flow.jwt.key.condition.JwtAlgKeyGetterCondition;
import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.security.flow.jwt.key.getter.JwtAlgKeyGetter;
import com.redhat.cleanbase.security.flow.jwt.key.model.KeyWithId;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;

public class DelegateJwtKeyGetter extends ConditionSelector<SignatureAlgorithm, JwtAlgKeyGetterCondition> implements JwtAlgKeyGetter {

    public DelegateJwtKeyGetter(List<JwtAlgKeyGetterCondition> keyGetterConditions) {
        super(keyGetterConditions);
    }

    @Override
    public KeyWithId getSignKey(SignatureAlgorithm signatureAlgorithm) {
        return getKeyAction(
                signatureAlgorithm,
                (condition) -> condition.getSignKey(signatureAlgorithm)
        );
    }


    @Override
    public KeyWithId getVerifyKey(SignatureAlgorithm signatureAlgorithm, String kid) {
        return getKeyAction(
                signatureAlgorithm,
                (condition) -> condition.getVerifyKey(signatureAlgorithm, kid)
        );
    }

    public KeyWithId getKeyAction(SignatureAlgorithm signatureAlgorithm, @NonNull Function<JwtAlgKeyGetterCondition, KeyWithId> keyWithIdFunction) {
        return getFirstCondition(signatureAlgorithm)
                .map(keyWithIdFunction)
                .orElse(null);
    }

}