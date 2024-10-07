package com.redhat.cleanbase.security.flow.jwt.datasource;

import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;

public interface RefreshTokenDataSource extends JwtDataSource {
    AccessToken getAccessToken();
}