package com.redhat.cleanbase.security.flow.jwt.key.model;

import lombok.*;

import java.security.Key;

@Data
@AllArgsConstructor
@Builder
public class KeyWithId {
    @NonNull
    private Key key;
    @NonNull
    private String kid;
}