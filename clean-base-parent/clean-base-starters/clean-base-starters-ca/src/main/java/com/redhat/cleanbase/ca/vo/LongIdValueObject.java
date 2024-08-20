package com.redhat.cleanbase.ca.vo;

import lombok.NonNull;

public record LongIdValueObject(@NonNull Long id) implements IdValueObject<Long> {
}