package com.redhat.cleanbase.account.application.port.usecase;

import com.redhat.cleanbase.account.application.port.usecase.model.SendMoneyCommand;
import com.redhat.cleanbase.ddd.usecase.UseCase;

public abstract class SendMoneyUseCase extends UseCase<SendMoneyCommand, Boolean> {
}
