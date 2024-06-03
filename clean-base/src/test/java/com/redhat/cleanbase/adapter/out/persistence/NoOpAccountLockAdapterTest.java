package com.redhat.cleanbase.adapter.out.persistence;

import com.redhat.cleanbase.application.domain.model.AccountDo;
import com.redhat.cleanbase.base.BaseTest;
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
            val accountId = new AccountDo.AccountId(1L);
            noOpAccountLock.lockAccount(accountId);
        });
    }

    @Test
    public void test_releaseAccount_happy() {
        Assertions.assertDoesNotThrow(() -> {
            val accountId = new AccountDo.AccountId(1L);
            noOpAccountLock.releaseAccount(accountId);
        });
    }

}
