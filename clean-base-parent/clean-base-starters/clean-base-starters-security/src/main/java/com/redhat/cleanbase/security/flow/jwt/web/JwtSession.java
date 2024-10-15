package com.redhat.cleanbase.security.flow.jwt.web;

import com.redhat.cleanbase.security.flow.jwt.cache.JwtCache;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.val;

import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.Callable;

public class JwtSession implements HttpSession {

    private final JwtCache jwtCache;
    private final ServletContext servletContext;

    public JwtSession(@NonNull JwtCache jwtCache, @NonNull ServletContext servletContext) {
        this.jwtCache = jwtCache;
        this.servletContext = servletContext;
    }

    @Override
    public long getCreationTime() {
        return jwtCache.getCreationTime();
    }

    @Override
    public String getId() {
        return jwtCache.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return jwtCache.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public Object getAttribute(String name) {
        return operate(() -> jwtCache.getAttribute(name));
    }

    private <O, E extends Exception> O operate(@NonNull SessionOperator<O, E> sessionOperator) throws E {
        ifInvalidateThrowException();
        try {
            return sessionOperator.call();
        } finally {
            setAccessTimeNow();
        }
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return operate(() -> {
            val attributeNames = jwtCache.getAttributeNames();
            if (attributeNames == null) {
                return null;
            }
            return Collections.enumeration(attributeNames);
        });
    }

    @Override
    public void setAttribute(String name, Object value) {
        operate(() -> {
            jwtCache.setAttribute(name, value);
            return null;
        });
    }

    @Override
    public void removeAttribute(String name) {
        operate(() -> {
            jwtCache.removeAttribute(name);
            return null;
        });
    }

    @Override
    public boolean isNew() {
        return getCreationTime() == getLastAccessedTime();
    }

    public boolean isExpire() {
        return System.currentTimeMillis() > jwtCache.getExpireTime();
    }

    public boolean isInvalidate() {
        return jwtCache.isInvalidate();
    }

    @Override
    public void invalidate() {
        jwtCache.invalidate();
    }

    private void ifInvalidateThrowException() {
        if (isInvalidate()) {
            throw new IllegalStateException("session 已無效");
        }
    }

    private void setAccessTimeNow() {
        jwtCache.setLastAccessedTime(System.currentTimeMillis());
    }

    @Override
    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException();
    }

    @FunctionalInterface
    public interface SessionOperator<O, E extends Exception> extends Callable<O> {
        @Override
        O call() throws E;
    }

}
