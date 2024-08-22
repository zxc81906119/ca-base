package com.redhat.cleanbase.account.adapter.in.controller;

import com.redhat.cleanbase.account.adapter.in.controller.converter.SendMoneyConverter;
import com.redhat.cleanbase.account.adapter.in.controller.model.SendMoneyDto;
import com.redhat.cleanbase.account.application.port.usecase.SendMoneyUseCase;
import com.redhat.cleanbase.exception.ParamValidateFailedException;
import com.redhat.cleanbase.exception.base.GenericException;
import com.redhat.cleanbase.validation.GenericValidator;
import com.redhat.cleanbase.web.response.GenericResponse;
import com.redhat.cleanbase.web.tracing.TracerWrapper;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// todo 會使用 interface , 與 openfeign 會共用
// todo request response dto 可能要放置共用區域管理, 放一個 module, 然後由　ｆｅｉｇｎ　ｃｌｉｅｎｔ　和　ｃｏｎｔｒｏｌｌｅｒ　ｍｏｄｕｌｅ　引入
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
            @RequestBody SendMoneyDto.Req req) throws GenericException {
        // todo request dto 驗證資料
        genericValidator.validate(req)
                .orThrow(ParamValidateFailedException::new);

        // todo 將 request dto 轉成　use case method input dto
        val sendMoneyCommand = sendMoneyConverter.dtoToCommand(req);

        // todo use case 方法調用,之後 use case 命名好辨識一點
        val isSendMoney = sendMoneyUseCase.action(sendMoneyCommand);

        val res = SendMoneyDto.Res.builder()
                .isSend(Boolean.TRUE.equals(isSendMoney))
                .build();

        return GenericResponse.ok(res, tracerWrapper.getCurrentSpanTraceId());
    }

}
