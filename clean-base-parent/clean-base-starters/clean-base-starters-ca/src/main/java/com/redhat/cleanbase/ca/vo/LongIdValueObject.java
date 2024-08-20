package com.redhat.cleanbase.ca.vo;


import lombok.NonNull;

public class LongIdValueObject extends IdValueObject<Long> {

    public LongIdValueObject(@NonNull Long id) {
        super(id);
    }

}