package com.redhat.cleanbase.security.flow.jwt.web;


import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpSession;

public class JwtRequest extends HttpServletRequestWrapper {

    private final JwtSession jwtSession;

    public JwtRequest(HttpServletRequest request, JwtCache jwtCache) {
        super(request);
        this.jwtSession = new JwtSession(jwtCache, request.getServletContext());
    }

    @Override
    public HttpSession getSession(boolean create) {
        // 如果是invalidate或是過期,就回傳null,jwt無法幫忙創,不然很怪
        if (jwtSession.isExpire() || jwtSession.isInvalidate()) {
            if (create) {
                throw new RuntimeException("需要重新登入拿到 session , 這裡不支援直接創建新 session");
            }
            return null;
        }
        return jwtSession;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }


}
