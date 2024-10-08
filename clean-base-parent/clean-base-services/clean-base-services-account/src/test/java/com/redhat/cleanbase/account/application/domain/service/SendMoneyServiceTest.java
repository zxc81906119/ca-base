package com.redhat.cleanbase.account.application.domain.service;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;
import com.redhat.cleanbase.account.application.domain.model.ActivityWindowVo;
import com.redhat.cleanbase.account.application.domain.model.MoneyVo;
import com.redhat.cleanbase.account.application.port.out.AccountLockPort;
import com.redhat.cleanbase.account.application.port.out.LoadAccountPort;
import com.redhat.cleanbase.account.application.port.out.UpdateAccountStatePort;
import com.redhat.cleanbase.account.application.port.usecase.model.SendMoneyCommand;
import com.redhat.cleanbase.test.base.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SendMoneyServiceTest extends BaseTest {

    @SpyBean
    private SendMoneyService sendMoneyService;
    @SpyBean
    private UpdateAccountStatePort updateAccountStatePort;
    @SpyBean
    private LoadAccountPort loadAccountPort;
    @SpyBean
    private AccountLockPort accountLockPort;

    @Test
    public void test_sendMoney_happy() throws Exception {
        val sourceAccountId =
                AccountDo.AccountId.builder()
                        .value(41L)
                        .build();
        val targetAccountId =
                AccountDo.AccountId.builder()
                        .value(42L)
                        .build();

        val moneyVo = MoneyVo.of(1000L);

        val sendMoneyCommand = new SendMoneyCommand(
                sourceAccountId,
                targetAccountId,
                moneyVo);

        val sourceAccountDo = AccountDo.withId(
                sourceAccountId,
                MoneyVo.of(4000L),
                new ActivityWindowVo(
                        new ArrayList<>()
                )
        );

        val targetAccountDo = AccountDo.withId(
                targetAccountId,
                MoneyVo.of(0L),
                new ActivityWindowVo(
                        new ArrayList<>()
                )
        );

        Mockito.doReturn(sourceAccountDo)
                .when(loadAccountPort)
                .loadAccount(Mockito.eq(sourceAccountId), Mockito.any(LocalDateTime.class));

        Mockito.doReturn(targetAccountDo)
                .when(loadAccountPort)
                .loadAccount(Mockito.eq(targetAccountId), Mockito.any(LocalDateTime.class));

        Mockito.doNothing()
                .when(accountLockPort)
                .lockAccount(Mockito.any(AccountDo.AccountId.class));

        Mockito.doNothing()
                .when(accountLockPort)
                .releaseAccount(Mockito.any(AccountDo.AccountId.class));

        Mockito.doNothing()
                .when(updateAccountStatePort)
                .updateActivities(Mockito.any(AccountDo.class));

        val sendMoney = sendMoneyService.action(sendMoneyCommand);
        Assertions.assertTrue(sendMoney);

        val sourceAccountActivityVos = sourceAccountDo.getActivityWindowVo().activities();
        Assertions.assertEquals(1, sourceAccountActivityVos.size());

        val sourceAccountActivityVo = sourceAccountActivityVos.get(0);
        Assertions.assertNull(sourceAccountActivityVo.getIdVO());
        Assertions.assertEquals(moneyVo, sourceAccountActivityVo.getMoneyVo());
        Assertions.assertEquals(sourceAccountId, sourceAccountActivityVo.getOwnerAccountId());
        Assertions.assertEquals(sourceAccountId, sourceAccountActivityVo.getSourceAccountId());
        Assertions.assertEquals(targetAccountId, sourceAccountActivityVo.getTargetAccountId());

        val targetAccountActivityVos = targetAccountDo.getActivityWindowVo().activities();
        Assertions.assertEquals(1, targetAccountActivityVos.size());

        val targetAccountActivityVo = targetAccountActivityVos.get(0);
        Assertions.assertNull(targetAccountActivityVo.getIdVO());
        Assertions.assertEquals(moneyVo, targetAccountActivityVo.getMoneyVo());
        Assertions.assertEquals(targetAccountId, targetAccountActivityVo.getOwnerAccountId());
        Assertions.assertEquals(sourceAccountId, targetAccountActivityVo.getSourceAccountId());
        Assertions.assertEquals(targetAccountId, targetAccountActivityVo.getTargetAccountId());

    }


}




