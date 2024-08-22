package com.redhat.cleanbase.ddd.vo;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class IdValueObject<Id> implements ValueObject {

    @NonNull
    private final Id value;

}