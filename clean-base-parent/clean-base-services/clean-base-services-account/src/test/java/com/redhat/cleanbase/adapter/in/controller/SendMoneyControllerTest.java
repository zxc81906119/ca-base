package com.redhat.cleanbase.adapter.in.controller;

import com.redhat.cleanbase.adapter.in.controller.model.SendMoneyDto;
import com.redhat.cleanbase.application.port.usecase.SendMoneyUseCase;
import com.redhat.cleanbase.application.port.usecase.model.SendMoneyCommand;
import com.redhat.cleanbase.base.BaseTest;
import com.redhat.cleanbase.common.response.code.ResponseCodeEnum;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class SendMoneyControllerTest extends BaseTest {

    @SpyBean
    private SendMoneyController sendMoneyController;

    @SpyBean
    private SendMoneyUseCase sendMoneyUseCase;

    @Test
    public void test_sendMoney_happy() {

        Mockito.doReturn(true)
                .when(sendMoneyUseCase)
                .sendMoney(Mockito.any(SendMoneyCommand.class));

        val req = SendMoneyDto.Req.builder()
                .sourceAccountId(41L)
                .targetAccountId(42L)
                .amount(500L)
                .build();
        val genericResponse = sendMoneyController.sendMoney(req);
        val res = genericResponse.getData();

        Assertions.assertEquals(Boolean.TRUE, res.getIsSend());
        Assertions.assertEquals(ResponseCodeEnum.API_SUCCESS.getValue(), genericResponse.getCode());
        Assertions.assertEquals(ResponseCodeEnum.API_SUCCESS.name(), genericResponse.getMessage());

    }

}
