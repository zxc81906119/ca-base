package com.redhat.cleanbase.security.flow.jwt.filter.impl;

import com.redhat.cleanbase.security.flow.jwt.filter.WrapRqBodyLoginFilter;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.DefaultLoginRequestDto;
import com.redhat.cleanbase.security.flow.jwt.filter.model.impl.LoginAuthToken;
import com.redhat.cleanbase.security.flow.jwt.converter.RequestDtoConverter;
import com.redhat.cleanbase.web.model.request.GenericRequest;
import lombok.NonNull;
import lombok.val;

public class DefaultLoginFilter extends WrapRqBodyLoginFilter<GenericRequest<DefaultLoginRequestDto>> {

    public DefaultLoginFilter(@NonNull RequestDtoConverter<GenericRequest<DefaultLoginRequestDto>> requestDtoConverter) {
        super(requestDtoConverter);
    }

    @Override
    protected LoginAuthToken rqDtoToLoginAuthToken(GenericRequest<DefaultLoginRequestDto> request) {
        val data = request.getData();
        val username = data.getUsername();
        val password = data.getPassword();
        return new LoginAuthToken(username, password);
    }

}
