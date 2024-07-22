package com.redhat.cleanbase.consumer.adapter.in.controller;

import com.redhat.cleanbase.consumer.application.port.usecase.CallTestApiUseCase;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Observed
@Slf4j
public class CallTestApiController {

    private final CallTestApiUseCase callTestApiUseCase;

    @RequestMapping("/test")
    public String test() throws Exception {
        return callTestApiUseCase.callApi();
    }

}
