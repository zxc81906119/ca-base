package com.redhat.cleanbase.security.flow.jwt.filter.handler.impl;

import com.redhat.cleanbase.security.flow.jwt.web.JwtSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class DefaultLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (request.getSession(false) instanceof JwtSession jwtSession) {
            jwtSession.invalidate();
        }
    }

}