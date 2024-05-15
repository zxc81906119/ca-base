package com.redhat.cleanbase.application.port.out;

import com.redhat.cleanbase.application.domain.model.AccountDo;


public interface AccountLockPort {

    void lockAccount(AccountDo.AccountId accountId);

    void releaseAccount(AccountDo.AccountId accountId);

}
