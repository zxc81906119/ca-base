package com.redhat.cleanbase.account.application.port.out;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;
import com.redhat.cleanbase.account.application.domain.model.AccountDo.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    AccountDo loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
