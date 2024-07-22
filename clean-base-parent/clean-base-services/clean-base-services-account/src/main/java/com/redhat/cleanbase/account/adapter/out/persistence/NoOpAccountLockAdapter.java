package com.redhat.cleanbase.account.adapter.out.persistence;

import com.redhat.cleanbase.account.application.domain.model.AccountDo.AccountId;
import com.redhat.cleanbase.account.application.port.out.AccountLockPort;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Component;

@Component
@Observed
public class NoOpAccountLockAdapter implements AccountLockPort {

    @Override
    public void lockAccount(AccountId accountId) {
        // do nothing
    }

    @Override
    public void releaseAccount(AccountId accountId) {
        // do nothing
    }

}
