package com.redhat.cleanbase.common.type;

public interface DataWithId<DATA, ID> {
    DATA data();
    ID id();
}