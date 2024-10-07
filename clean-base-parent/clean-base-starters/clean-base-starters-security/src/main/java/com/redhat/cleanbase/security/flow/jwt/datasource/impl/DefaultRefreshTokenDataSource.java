package com.redhat.cleanbase.security.flow.jwt.datasource.impl;

import com.redhat.cleanbase.security.flow.jwt.datasource.RefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import lombok.Data;

@Data
public class DefaultRefreshTokenDataSource implements RefreshTokenDataSource {
    private AccessToken accessToken;
}