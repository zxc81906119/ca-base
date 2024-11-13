package com.redhat.cleanbase.web.servlet.controller.advice.response.impl;

import com.redhat.cleanbase.common.mapper.config.BaseMapperConfig;
import com.redhat.cleanbase.web.model.info.impl.GenericAppInfo;
import com.redhat.cleanbase.web.model.info.impl.ServiceAppInfo;
import com.redhat.cleanbase.web.model.response.GenericResponse;
import com.redhat.cleanbase.web.servlet.context.impl.AppInfoContext;
import com.redhat.cleanbase.web.tracing.TracerWrapper;
import com.redhat.cleanbase.web.servlet.controller.advice.response.WrapResponseBodyAdvice;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GenericResponseBodyAdvice extends WrapResponseBodyAdvice<ServiceAppInfo, AppInfoContext, GenericResponse<?>> {

    private final TracerWrapper tracerWrapper;

    public GenericResponseBodyAdvice(
            TracerWrapper tracerWrapper,
            AppInfoContext appInfoContext
    ) {
        super(appInfoContext);
        this.tracerWrapper = tracerWrapper;
    }

    @Override
    protected GenericResponse<?> toWrapResponse(Object body) {
        return body instanceof GenericResponse<?> genericResponse
                ? genericResponse
                : GenericResponse.ok(body);
    }

    @Override
    protected void setServiceAppInfoFromContext(AppInfoContext appInfoContext, ServiceAppInfo serviceAppInfo) {
        Converter.INSTANCE.patchUpdate(appInfoContext.getServiceAppInfo(), serviceAppInfo);
        Converter.INSTANCE.patchUpdate(appInfoContext.getGenericAppInfo(), serviceAppInfo);
        serviceAppInfo.setTraceId(tracerWrapper.getCurrentSpanTraceId());
    }

    @Mapper(
            config = BaseMapperConfig.class
    )
    public interface Converter {
        Converter INSTANCE = Mappers.getMapper(Converter.class);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void patchUpdate(ServiceAppInfo serviceAppInfo, @MappingTarget ServiceAppInfo serviceAppInfo1);

        @Mapping(target = "code", ignore = true)
        @Mapping(target = "title", ignore = true)
        @Mapping(target = "message", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "traceId", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void patchUpdate(GenericAppInfo genericAppInfo, @MappingTarget ServiceAppInfo serviceAppInfo);

    }

}