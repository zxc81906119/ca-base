package com.redhat.cleanbase.account.adapter.in.controller;

import com.redhat.cleanbase.account.adapter.in.controller.model.SendMoneyDto;
import com.redhat.cleanbase.account.application.port.usecase.SendMoneyUseCase;
import com.redhat.cleanbase.account.application.port.usecase.model.SendMoneyCommand;
import com.redhat.cleanbase.code.response.ResponseCodeEnum;
import com.redhat.cleanbase.test.base.BaseTest;
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
    public void test_sendMoney_happy() throws Exception {

        Mockito.doReturn(true)
                .when(sendMoneyUseCase)
                .action(Mockito.any(SendMoneyCommand.class));

        val req = SendMoneyDto.Req.builder()
                .sourceAccountId(41L)
                .targetAccountId(42L)
                .amount(500L)
                .build();
        val genericResponse = sendMoneyController.sendMoney(req);
        val res = genericResponse.getData();

        Assertions.assertEquals(Boolean.TRUE, res.getIsSend());
        Assertions.assertEquals(ResponseCodeEnum.API_SUCCESS.getValue(), genericResponse.getServiceAppInfo().getCode());

    }

}
