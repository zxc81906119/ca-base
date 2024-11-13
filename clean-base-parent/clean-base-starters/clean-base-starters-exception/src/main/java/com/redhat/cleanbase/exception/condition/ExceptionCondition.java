package com.redhat.cleanbase.exception.condition;

import com.redhat.cleanbase.exception.handler.ExceptionHandler;
import com.redhat.cleanbase.common.type.Identifiable;

public interface ExceptionCondition<D, R, T extends Exception> extends Identifiable<Class<T>>, ExceptionHandler<D, R, T> {
}
