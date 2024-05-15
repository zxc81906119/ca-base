package com.redhat.cleanbase.application.port.out.xxx;

import com.redhat.cleanbase.application.domain.xxx.model.AccountDo;


public interface AccountLockPort {

    void lockAccount(AccountDo.AccountId accountId);

    void releaseAccount(AccountDo.AccountId accountId);

}
