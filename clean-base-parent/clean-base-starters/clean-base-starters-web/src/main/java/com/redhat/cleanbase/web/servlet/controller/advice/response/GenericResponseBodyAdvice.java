package com.redhat.cleanbase.web.servlet.controller.advice.response;

import com.redhat.cleanbase.common.constants.GenericConstants;
import com.redhat.cleanbase.common.utils.ReflectionUtils;
import com.redhat.cleanbase.web.response.GenericResponse;
import com.redhat.cleanbase.web.tracing.TracerWrapper;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GenericResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final TracerWrapper tracerWrapper;

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // controller 類
        val declaringClass = returnType.getDeclaringClass();

        // 非本系統不管他
        val packageName = declaringClass.getPackageName();
        if (!packageName.startsWith(GenericConstants.BASE_PACKAGE_NAME)) {
            return false;
        }

        // 忽略的不管它
        if (ReflectionUtils.findAnnotation(declaringClass, IgnoreResponseAdvice.class) != null) {
            return false;
        }

        if (ReflectionUtils.findAnnotation(declaringClass, ResponseBody.class) != null) {
            return true;
        }

        // api method , 應該是不會為空,但是 ide 不知道,所以還是這樣檢查
        val method = returnType.getMethod();
        if (method == null) {
            return false;
        }

        // 如果 api method 有忽略就不管
        if (ReflectionUtils.findAnnotation(method, IgnoreResponseAdvice.class) != null) {
            return false;
        }

        // 如果是 response body 就管
        return ReflectionUtils.findAnnotation(method, ResponseBody.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // todo 未來可以依照 media type 多判斷是否要轉,目前是都以 application json or xml 去做
        return body instanceof GenericResponse ?
                body
                : GenericResponse.ok(body, tracerWrapper.getCurrentSpanTraceId());
    }
}