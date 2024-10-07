package com.redhat.cleanbase.security.flow.jwt.cache;


import java.util.Map;
import java.util.Set;

public interface JwtCache {
    long getCreationTime();

    long getExpireTime();

    void setExpireTime(long expireTime);

    String getId();

    Set<String> getAttributeNames();

    Map<String, Object> getAttributes();

    void setAttributes(Map<String, Object> map);

    boolean isInvalidate();

    void invalidate();

    long getLastAccessedTime();

    void setLastAccessedTime(long lastAccessedTime);

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    void removeAttribute(String name);
}
