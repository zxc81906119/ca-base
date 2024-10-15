package com.redhat.cleanbase.security.flow.jwt.token.getter;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface RqJwtTokenGetter {
    Optional<String> getToken(HttpServletRequest request);
}
