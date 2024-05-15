package com.redhat.cleanbase.application.port.out.xxx;

import com.redhat.cleanbase.application.domain.xxx.model.AccountDo;
import com.redhat.cleanbase.application.domain.xxx.model.AccountDo.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    AccountDo loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
