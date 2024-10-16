package com.redhat.cleanbase.common.type;

@FunctionalInterface
public interface Identifiable<Id> {
    Id getIdentifier();
}

