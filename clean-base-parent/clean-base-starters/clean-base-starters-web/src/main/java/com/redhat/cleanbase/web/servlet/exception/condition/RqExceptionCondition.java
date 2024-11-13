package com.redhat.cleanbase.web.servlet.exception.condition;

import com.redhat.cleanbase.exception.condition.ExceptionCondition;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface RqExceptionCondition<E extends Exception> extends ExceptionCondition<HttpServletRequest, ResponseEntity<?>, E> {
}
