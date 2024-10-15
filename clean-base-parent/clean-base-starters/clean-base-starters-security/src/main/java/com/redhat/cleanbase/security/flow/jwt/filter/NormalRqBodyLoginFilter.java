package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.filter.model.LoginRequestDto;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import lombok.NonNull;

public abstract class NormalRqBodyLoginFilter<T extends LoginRequestDto> extends BaseLoginFilter<T> {

    public NormalRqBodyLoginFilter(@NonNull RequestDtoConverter<T> requestDtoConverter) {
        super(requestDtoConverter);
    }

}
