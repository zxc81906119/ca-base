package com.redhat.cleanbase.security.flow.jwt.filter;

import com.redhat.cleanbase.security.flow.jwt.filter.model.LoginRequestDto;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestConverter;
import lombok.NonNull;

public abstract class NormalRqBodyLoginFilter<T extends LoginRequestDto> extends BaseLoginFilter<T> {

    public NormalRqBodyLoginFilter(@NonNull RequestConverter<T> requestConverter) {
        super(requestConverter);
    }

}
