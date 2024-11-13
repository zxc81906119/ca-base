package com.redhat.cleanbase.security.flow.jwt.converter.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redhat.cleanbase.convert.parser.JacksonJsonParser;
import com.redhat.cleanbase.web.model.request.GenericRequest;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.DefaultLoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRequestDtoConverter implements RequestDtoConverter<GenericRequest<DefaultLoginRequestDto>> {

    @NonNull
    private final JacksonJsonParser<?> jacksonJsonParser;

    @Override
    public GenericRequest<DefaultLoginRequestDto> convert(HttpServletRequest request) throws Exception {
        return jacksonJsonParser.parse(
                request.getInputStream(),
                new TypeReference<>() {
                }
        );
    }
}
