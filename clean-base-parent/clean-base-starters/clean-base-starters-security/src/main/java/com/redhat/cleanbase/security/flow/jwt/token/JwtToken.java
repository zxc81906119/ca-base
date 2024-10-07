package com.redhat.cleanbase.security.flow.jwt.token;

import java.util.Date;
import java.util.Map;

public interface JwtToken {
    String getId();

    void setId(String id);

    String getSubject();

    void setSubject(String subject);

    String getTokenString();

    void setTokenString(String tokenString);

    Map<String, Object> getPayload();

    void setPayload(Map<String, Object> payload);

    Date getCreationTime();

    void setCreationTime(Date creationTime);

    Date getExpireTime();

    void setExpireTime(Date date);
}