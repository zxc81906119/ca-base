package com.redhat.cleanbase.log.field;

import java.util.List;

@FunctionalInterface
public interface CustomizedLogFieldsGetter {
    List<? extends CustomizedLogField> get();
}