package com.redhat.cleanbase.account.application.port.usecase.model;

import com.redhat.cleanbase.account.application.domain.model.AccountDo;
import com.redhat.cleanbase.account.application.domain.model.MoneyVo;
import com.redhat.cleanbase.ddd.readmodel.ReadModel;
import com.redhat.cleanbase.exception.ParamValidateFailedException;
import com.redhat.cleanbase.validation.context.GenericValidationContext;
import jakarta.validation.constraints.NotNull;

public record SendMoneyCommand(
        @NotNull AccountDo.AccountId sourceAccountId,
        @NotNull AccountDo.AccountId targetAccountId,
        @NotNull MoneyVo moneyVo
) implements ReadModel {
    @Override
    public void validate() {
        GenericValidationContext.getValidator()
                .validate(this)
                .orThrow(ParamValidateFailedException::new);
    }
}
