package com.redhat.cleanbase.ca.usecase;

import com.redhat.cleanbase.ca.readmodel.ReadModel;
import com.redhat.cleanbase.exception.base.GenericException;

public interface BaseUseCase<R extends ReadModel, T> {
    T action(R readModel) throws GenericException;
}