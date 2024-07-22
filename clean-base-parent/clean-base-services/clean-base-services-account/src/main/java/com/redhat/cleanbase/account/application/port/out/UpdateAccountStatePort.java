package com.redhat.cleanbase.account.application.port.out;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;

public interface UpdateAccountStatePort {

    void updateActivities(AccountDo accountDo);

}
