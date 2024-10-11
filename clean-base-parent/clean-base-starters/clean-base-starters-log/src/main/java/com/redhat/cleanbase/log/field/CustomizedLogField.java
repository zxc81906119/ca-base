package com.redhat.cleanbase.log.field;

import com.redhat.cleanbase.common.type.Identifiable;

public interface CustomizedLogField extends Identifiable<String> {

    @Override
    default String getIdentifier() {
        return name();
    }

    String name();
}