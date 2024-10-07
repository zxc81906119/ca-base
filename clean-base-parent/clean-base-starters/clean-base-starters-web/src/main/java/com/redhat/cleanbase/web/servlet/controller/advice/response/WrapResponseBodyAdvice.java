package com.redhat.cleanbase.web.servlet.controller.advice.response;

import com.redhat.cleanbase.common.constants.GenericConstants;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.web.model.info.IServiceAppInfo;
import com.redhat.cleanbase.web.model.response.WrapResponse;
import com.redhat.cleanbase.web.servlet.context.IAppInfoContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RequiredArgsConstructor
public abstract class WrapResponseBodyAdvice<S extends IServiceAppInfo, A extends IAppInfoContext<?, S, ?>, R extends WrapResponse<?, S>> implements ResponseBodyAdvice<Object> {
    @NonNull
    private final A appInfoContext;

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        val method = returnType.getMethod();
        if (method == null) {
            return false;
        }

        // controller 類
        val containingClass = returnType.getContainingClass();

        // 非本系統不管他
        val packageName = containingClass.getPackageName();
        if (!packageName.startsWith(GenericConstants.BASE_PACKAGE_NAME)) {
            return false;
        }

        // 忽略的不管它
        if (ReflectionUtils.findAnnotation(containingClass, IgnoreResponseAdvice.class) != null) {
            return false;
        }

        // 如果 api method 有忽略就不管
        if (ReflectionUtils.findAnnotation(method, IgnoreResponseAdvice.class) != null) {
            return false;
        }

        if (ReflectionUtils.findAnnotation(containingClass, ResponseBody.class) != null) {
            return true;
        }

        // 如果是 response body 就管
        return ReflectionUtils.findAnnotation(method, ResponseBody.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        val wrapResponse = toWrapResponse(body);
        setServiceAppInfoFromContext(appInfoContext, wrapResponse.findServiceAppInfoOrNew());
        return wrapResponse;
    }

    protected abstract void setServiceAppInfoFromContext(A a, S s);

    protected abstract R toWrapResponse(Object body);

}