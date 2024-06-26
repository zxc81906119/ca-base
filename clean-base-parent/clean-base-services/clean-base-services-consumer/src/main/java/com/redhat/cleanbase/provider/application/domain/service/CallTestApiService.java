package com.redhat.cleanbase.provider.application.domain.service;

import com.redhat.cleanbase.provider.application.port.out.CallTestApiPort;
import com.redhat.cleanbase.provider.application.port.usecase.CallTestApiUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallTestApiService implements CallTestApiUseCase {
    private final CallTestApiPort callTestApiPort;

    @Override
    public String callApi() throws Exception {
        return callTestApiPort.callApi();
    }
}
