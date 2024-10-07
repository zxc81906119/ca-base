package com.redhat.cleanbase.security.flow.jwt.key.getter;

import com.redhat.cleanbase.security.flow.jwt.key.model.KeyWithId;
import io.jsonwebtoken.SignatureAlgorithm;

public interface JwtAlgKeyGetter {
    KeyWithId getSignKey(SignatureAlgorithm signatureAlgorithm);

    KeyWithId getVerifyKey(SignatureAlgorithm signatureAlgorithm, String kid);

}