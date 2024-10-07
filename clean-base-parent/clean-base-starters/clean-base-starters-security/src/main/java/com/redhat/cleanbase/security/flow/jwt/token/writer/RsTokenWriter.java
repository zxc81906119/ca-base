package com.redhat.cleanbase.security.flow.jwt.token.writer;

import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;

public interface RsTokenWriter {

    void write(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken);

}
