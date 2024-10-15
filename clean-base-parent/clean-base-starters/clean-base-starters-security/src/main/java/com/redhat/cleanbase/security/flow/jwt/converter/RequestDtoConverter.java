package com.redhat.cleanbase.security.flow.jwt.converter;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface RequestDtoConverter<T> {
    T convert(HttpServletRequest request) throws Exception;
}
