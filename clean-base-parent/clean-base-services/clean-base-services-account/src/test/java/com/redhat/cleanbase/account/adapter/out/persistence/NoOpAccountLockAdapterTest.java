package com.redhat.cleanbase.account.adapter.out.persistence;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;
import com.redhat.cleanbase.test.base.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class NoOpAccountLockAdapterTest extends BaseTest {

    @SpyBean
    public NoOpAccountLockAdapter noOpAccountLock;

    @Test
    public void test_lockAccount_happy() {
        Assertions.assertDoesNotThrow(() -> {
            val accountId =
                    AccountDo.AccountId.builder()
                            .value(1L)
                            .build();
            noOpAccountLock.lockAccount(accountId);
        });
    }

    @Test
    public void test_releaseAccount_happy() {
        Assertions.assertDoesNotThrow(() -> {
            val accountId = AccountDo.AccountId.builder()
                    .value(1L)
                    .build();
            noOpAccountLock.releaseAccount(accountId);
        });
    }

}
