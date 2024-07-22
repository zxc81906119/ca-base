package com.redhat.cleanbase.account.adapter.in.controller;

import com.redhat.cleanbase.account.adapter.in.controller.converter.SendMoneyConverter;
import com.redhat.cleanbase.account.adapter.in.controller.model.SendMoneyDto;
import com.redhat.cleanbase.account.application.port.usecase.SendMoneyUseCase;
import com.redhat.cleanbase.common.exception.ParamValidateFailedException;
import com.redhat.cleanbase.common.response.GenericResponse;
import com.redhat.cleanbase.common.tracing.TracerWrapper;
import com.redhat.cleanbase.validation.GenericValidator;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Observed
public class SendMoneyController {

    private final SendMoneyConverter sendMoneyConverter;

    private final SendMoneyUseCase sendMoneyUseCase;

    private final GenericValidator genericValidator;

    private final TracerWrapper tracerWrapper;

    @Operation(description = "轉帳")
    @PostMapping
    public GenericResponse<SendMoneyDto.Res> sendMoney(
            @RequestBody SendMoneyDto.Req req) {

        genericValidator.validate(req)
                .orThrow(ParamValidateFailedException::new);

        val sendMoneyCommand = sendMoneyConverter.dtoToCommand(req);

        val isSendMoney = sendMoneyUseCase.sendMoney(sendMoneyCommand);

        val res = SendMoneyDto.Res.builder()
                .isSend(isSendMoney)
                .build();

        return GenericResponse.ok(res, tracerWrapper.getCurrentSpanTraceId());
    }

}
