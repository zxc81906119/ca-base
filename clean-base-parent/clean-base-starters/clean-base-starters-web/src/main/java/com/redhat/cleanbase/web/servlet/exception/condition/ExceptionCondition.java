package com.redhat.cleanbase.web.servlet.exception.condition;

import com.redhat.cleanbase.common.type.Identifiable;
import com.redhat.cleanbase.exception.handler.ExceptionHandler;

public interface ExceptionCondition<D, T extends Exception> extends Identifiable<Class<T>>, ExceptionHandler<D, T> {
}
