package com.redhat.cleanbase.security.flow.jwt.key.model.impl;

import com.redhat.cleanbase.security.flow.jwt.key.model.KeyInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(
        onlyExplicitlyIncluded = true
)
@Data
public class DefaultKeyInfo implements KeyInfo {
    // 金鑰唯一標示
    @EqualsAndHashCode.Include
    private String kid;
    // 對稱加密與非對稱加密皆有
    private String privateKey;
    // 只有非對稱加密才有
    private String publicKey;

}