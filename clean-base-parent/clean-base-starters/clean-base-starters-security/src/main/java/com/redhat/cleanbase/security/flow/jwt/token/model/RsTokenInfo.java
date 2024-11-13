package com.redhat.cleanbase.security.flow.jwt.token.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RsTokenInfo {
    private String accessTokenParamName;
    private String refreshTokenParamName;
    private String accessTokenString;
    private Optional<String> refreshTokenString;

    private String accessTokenExpirationParamName;
    private String refreshTokenExpirationParamName;
    private Long accessTokenExpiration;
    private Optional<Long> refreshTokenExpiration;

}
