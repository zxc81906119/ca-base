package com.redhat.cleanbase.security.flow.jwt.token.impl;

import com.redhat.cleanbase.security.flow.jwt.token.RefreshToken;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class DefaultRefreshToken implements RefreshToken {
    @NotBlank
    private String id;

    @NotBlank
    private String subject;

    private String tokenString;

    @NotNull
    @Size(min = 1)
    private Map<String, Object> payload;

    @NotNull
    private Date creationTime;

    @NotNull
    @Past
    private Date expireTime;
}