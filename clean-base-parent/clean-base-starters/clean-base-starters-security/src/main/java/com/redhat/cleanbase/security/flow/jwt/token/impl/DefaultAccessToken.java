package com.redhat.cleanbase.security.flow.jwt.token.impl;

import com.redhat.cleanbase.security.flow.jwt.datasource.impl.DefaultRefreshTokenDataSource;
import com.redhat.cleanbase.security.flow.jwt.token.AccessToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultAccessToken extends DefaultRefreshTokenDataSource implements AccessToken {
    @NotBlank
    private String id;

    @NotBlank
    private String subject;

    private String tokenString;
    private Map<String, Object> payload;

    @NotNull
    private Date creationTime;

    @NotNull
    @Past
    private Date expireTime;

    public DefaultAccessToken() {
        setAccessToken(this);
    }
}