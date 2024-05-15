package com.redhat.cleanbase.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public final class WebUtil {
    private WebUtil() {
        throw new UnsupportedOperationException();
    }

    public static Optional<HttpServletRequest> getHttpServletRequest() {
        return getServletRequestAttributes()
                .map(ServletRequestAttributes::getRequest);
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
