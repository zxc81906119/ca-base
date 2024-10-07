package com.redhat.cleanbase.security.flow.jwt.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RsTokenInfo {
    private String accessTokenParamName;
    private String refreshTokenParamName;
    private String accessTokenString;
    private String refreshTokenString;
}
