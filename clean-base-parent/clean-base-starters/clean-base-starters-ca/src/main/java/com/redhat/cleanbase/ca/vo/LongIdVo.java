package com.redhat.cleanbase.ca.vo;

import lombok.NonNull;

public record LongIdVo(@NonNull Long id) implements IdVo<Long> {
}