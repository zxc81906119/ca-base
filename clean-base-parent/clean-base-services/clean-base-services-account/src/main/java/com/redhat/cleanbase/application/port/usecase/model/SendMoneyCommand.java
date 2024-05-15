package com.redhat.cleanbase.application.port.usecase.model;

import com.redhat.cleanbase.application.domain.xxx.model.AccountDo;
import com.redhat.cleanbase.application.domain.xxx.model.MoneyVo;
import com.redhat.cleanbase.common.validation.context.GlobalValidatorContext;
import jakarta.validation.constraints.NotNull;

public record SendMoneyCommand(
        @NotNull AccountDo.AccountId sourceAccountId,
        @NotNull AccountDo.AccountId targetAccountId,
        @NotNull MoneyVo moneyVo) {

    public SendMoneyCommand(AccountDo.AccountId sourceAccountId,
                            AccountDo.AccountId targetAccountId,
                            MoneyVo moneyVo) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.moneyVo = moneyVo;
        GlobalValidatorContext.getValidator()
                .validateOrThrow(this);
    }
}
