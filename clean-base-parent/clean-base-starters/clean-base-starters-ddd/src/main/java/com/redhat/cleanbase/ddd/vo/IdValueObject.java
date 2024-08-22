package com.redhat.cleanbase.ddd.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class IdValueObject<Id> implements ValueObject {

    @NonNull
    private final Id value;

}