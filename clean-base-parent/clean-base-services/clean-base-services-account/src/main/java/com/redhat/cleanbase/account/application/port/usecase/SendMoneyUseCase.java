package com.redhat.cleanbase.account.application.port.usecase;

import com.redhat.cleanbase.account.application.port.usecase.model.SendMoneyCommand;

public interface SendMoneyUseCase {

	boolean sendMoney(SendMoneyCommand command);

}
