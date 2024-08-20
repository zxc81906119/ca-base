package com.redhat.cleanbase.ca.vo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class IdValueObject<ID> implements ValueObject {

    @NonNull
    private final ID id;

    public ID id() {
        return id;
    }

}