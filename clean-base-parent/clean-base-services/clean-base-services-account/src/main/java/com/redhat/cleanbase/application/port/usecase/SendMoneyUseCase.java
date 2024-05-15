package com.redhat.cleanbase.application.port.usecase;

import com.redhat.cleanbase.application.port.usecase.model.SendMoneyCommand;

public interface SendMoneyUseCase {

	boolean sendMoney(SendMoneyCommand command);

}
