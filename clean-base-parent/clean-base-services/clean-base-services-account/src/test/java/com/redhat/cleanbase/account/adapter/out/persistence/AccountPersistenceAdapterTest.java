package com.redhat.cleanbase.account.adapter.out.persistence;

import com.redhat.cleanbase.account.adapter.out.persistence.repository.ActivityRepository;
import com.redhat.cleanbase.account.adapter.out.persistence.repository.SpringDataAccountRepository;
import com.redhat.cleanbase.account.adapter.out.persistence.repository.model.AccountPo;
import com.redhat.cleanbase.account.adapter.out.persistence.repository.model.ActivityPo;
import com.redhat.cleanbase.account.application.domain.model.AccountDo;
import com.redhat.cleanbase.account.application.domain.model.ActivityDo;
import com.redhat.cleanbase.account.application.domain.model.ActivityWindowVo;
import com.redhat.cleanbase.account.application.domain.model.MoneyVo;
import com.redhat.cleanbase.test.base.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AccountPersistenceAdapterTest extends BaseTest {

    @SpyBean
    private AccountPersistenceAdapter accountPersistenceAdapter;
    @SpyBean
    private SpringDataAccountRepository accountRepository;
    @SpyBean
    private ActivityRepository activityRepository;

    @Test
    public void test_loadAccount_happy() {
        val accountIdValue = 30L;
        val accountId =
                AccountDo.AccountId.builder()
                        .value(accountIdValue)
                        .build();
        val accountPo = new AccountPo(accountIdValue);

        Mockito.doReturn(Optional.of(accountPo))
                .when(accountRepository)
                .findById(Mockito.any(Long.class));

        val activityPos = List.of(
                new ActivityPo(1L,
                        LocalDateTime.now(),
                        accountPo.getId(),
                        accountPo.getId(),
                        31L,
                        1000L)
        );
        Mockito.doReturn(activityPos)
                .when(activityRepository)
                .findByOwnerSince(Mockito.any(Long.class), Mockito.any(LocalDateTime.class));

        val depositBalance = 3000L;
        Mockito.doReturn(Optional.of(depositBalance))
                .when(activityRepository)
                .getDepositBalanceUntil(Mockito.any(Long.class), Mockito.any(LocalDateTime.class));

        val withdrawalBalance = 2000L;
        Mockito.doReturn(Optional.of(withdrawalBalance))
                .when(activityRepository)
                .getWithdrawalBalanceUntil(Mockito.any(Long.class), Mockito.any(LocalDateTime.class));

        val accountDo = accountPersistenceAdapter.loadAccount(accountId, LocalDateTime.now());
        Assertions.assertEquals(accountIdValue,
                accountDo.getId()
                        .map(AccountDo.AccountId::getValue)
                        .orElse(null)
        );

        Assertions.assertEquals(depositBalance - withdrawalBalance, accountDo.getBaselineBalance().amount().longValue());
        val activityVos = accountDo.getActivityWindowVo().activities();
        Assertions.assertEquals(activityPos.size(), activityVos.size());

        for (int i = 0; i < activityPos.size(); i++) {
            val activityPo = activityPos.get(i);
            val activityVo = activityVos.get(i);
            Assertions.assertEquals(activityPo.getId(), activityVo.getId().value());
            Assertions.assertEquals(activityPo.getTimestamp(), activityVo.getTimestamp());
            Assertions.assertEquals(activityPo.getOwnerAccountId(), activityVo.getOwnerAccountId().getValue());
            Assertions.assertEquals(activityPo.getSourceAccountId(), activityVo.getSourceAccountId().getValue());
            Assertions.assertEquals(activityPo.getTargetAccountId(), activityVo.getTargetAccountId().getValue());
            Assertions.assertEquals(activityPo.getAmount(), activityVo.getMoneyVo().amount().longValue());
        }
    }

    @Test
    public void updateActivities() {

        Mockito.doReturn(null)
                .when(activityRepository)
                .save(Mockito.any(ActivityPo.class));

        val accountId = 30L;
        val accountDo = AccountDo.withId(
                AccountDo.AccountId.builder()
                        .value(accountId)
                        .build(),
                MoneyVo.of(0L),
                new ActivityWindowVo(
                        List.of(
                                new ActivityDo(
                                        AccountDo.AccountId.builder()
                                                .value(accountId)
                                                .build(),
                                        AccountDo.AccountId.builder()
                                                .value(31L)
                                                .build(),
                                        AccountDo.AccountId.builder()
                                                .value(accountId)
                                                .build(),
                                        LocalDateTime.now(),
                                        MoneyVo.of(300L)
                                )
                        )
                )
        );

        Assertions.assertDoesNotThrow(() ->
                accountPersistenceAdapter.updateActivities(accountDo));
    }

}
