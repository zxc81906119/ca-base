package com.redhat.cleanbase.web.servlet.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public final class WebUtils {
    private WebUtils() {
        throw new UnsupportedOperationException();
    }

    public static Optional<HttpServletRequest> getHttpServletRequest() {
        return getServletRequestAttributes()
                .map(ServletRequestAttributes::getRequest);
    }

    public static Optional<String> getRequestURI() {
        return getHttpServletRequest()
                .map(HttpServletRequest::getRequestURI);
    }

    public static Optional<HttpServletResponse> getHttpServletResponse() {
        return getServletRequestAttributes()
                .map(ServletRequestAttributes::getResponse);
    }

    private static Optional<ServletRequestAttributes> getServletRequestAttributes() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast);
    }

}
