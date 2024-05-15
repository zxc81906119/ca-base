package com.redhat.cleanbase.adapter.out.persistence;

import com.redhat.cleanbase.application.domain.xxx.model.AccountDo.AccountId;
import com.redhat.cleanbase.application.port.out.xxx.AccountLockPort;
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
