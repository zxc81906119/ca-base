package com.redhat.cleanbase.security.flow.jwt.key.condition;

import com.redhat.cleanbase.security.flow.jwt.key.model.KeyInfo;
import com.redhat.cleanbase.security.flow.jwt.key.store.JwtKeyStore;
import com.redhat.cleanbase.security.flow.jwt.key.model.KeyWithId;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Getter
@RequiredArgsConstructor
public abstract class AbstractJwtAlgKeyGetterCondition<I extends KeyInfo> implements JwtAlgKeyGetterCondition {

    private final JwtKeyStore<I> jwtKeyStore;

    @Override
    public KeyWithId getSignKey(SignatureAlgorithm signatureAlgorithm) {
        val i = jwtKeyStore.findRandom(signatureAlgorithm);
        return getSignKey(i);
    }

    protected abstract KeyWithId getSignKey(I i);

    @Override
    public KeyWithId getVerifyKey(SignatureAlgorithm signatureAlgorithm, String kid) {
        val i = jwtKeyStore.find(signatureAlgorithm, kid);
        return getVerifyKey(i);
    }

    protected abstract KeyWithId getVerifyKey(I i);


}
