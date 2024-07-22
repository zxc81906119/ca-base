package com.redhat.cleanbase.account.application.port.out;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;


public interface AccountLockPort {

    void lockAccount(AccountDo.AccountId accountId);

    void releaseAccount(AccountDo.AccountId accountId);

}
