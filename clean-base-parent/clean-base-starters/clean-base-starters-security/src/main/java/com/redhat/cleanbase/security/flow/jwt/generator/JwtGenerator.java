package com.redhat.cleanbase.security.flow.jwt.generator;

import com.redhat.cleanbase.security.flow.jwt.datasource.JwtDataSource;
import com.redhat.cleanbase.security.flow.jwt.token.JwtToken;

public interface JwtGenerator<T extends JwtToken, TS extends JwtDataSource> {
    T generate(TS ts);
}
