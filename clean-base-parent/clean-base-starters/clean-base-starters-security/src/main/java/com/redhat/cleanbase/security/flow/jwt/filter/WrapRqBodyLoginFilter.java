package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import com.redhat.cleanbase.security.flow.jwt.filter.model.LoginRequestDto;
import com.redhat.cleanbase.web.model.request.WrapRequest;
import lombok.NonNull;

public abstract class WrapRqBodyLoginFilter<W extends WrapRequest<? extends LoginRequestDto, ?>> extends BaseLoginFilter<W> {
    public WrapRqBodyLoginFilter(@NonNull RequestDtoConverter<W> requestDtoConverter) {
        super(requestDtoConverter);
    }
}
