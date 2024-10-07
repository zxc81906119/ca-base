package com.redhat.cleanbase.security.flow.jwt.datasource.impl;

import com.redhat.cleanbase.security.flow.jwt.datasource.AccessTokenDataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DefaultAccessTokenDataSource implements AccessTokenDataSource {
    private String jwtId;
    private String userId;
}