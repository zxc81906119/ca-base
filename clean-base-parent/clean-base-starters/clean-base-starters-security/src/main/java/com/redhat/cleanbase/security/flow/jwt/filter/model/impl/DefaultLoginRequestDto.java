package com.redhat.cleanbase.security.flow.jwt.filter.model.impl;

import com.redhat.cleanbase.security.flow.jwt.filter.model.LoginRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DefaultLoginRequestDto implements LoginRequestDto {
    private String username;
    private String password;
}
