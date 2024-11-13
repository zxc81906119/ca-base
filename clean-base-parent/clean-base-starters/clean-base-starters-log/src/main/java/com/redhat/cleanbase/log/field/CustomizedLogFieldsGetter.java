package com.redhat.cleanbase.log.field;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface CustomizedLogFieldsGetter extends Supplier<List<CustomizedLogField>> {
}