package com.redhat.cleanbase.web.servlet.exception.condition;

import com.redhat.cleanbase.exception.condition.ExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;

public interface RqExceptionCondition<E extends Exception> extends ExceptionCondition<HttpServletRequest, E> {
}
