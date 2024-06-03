package com.redhat.cleanbase.application.port.out;

import com.redhat.cleanbase.application.domain.model.AccountDo;

public interface UpdateAccountStatePort {

    void updateActivities(AccountDo accountDo);

}
