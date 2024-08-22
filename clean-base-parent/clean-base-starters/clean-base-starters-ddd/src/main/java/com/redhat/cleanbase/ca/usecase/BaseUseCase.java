package com.redhat.cleanbase.ca.usecase;

import com.redhat.cleanbase.ca.readmodel.ReadModel;
import com.redhat.cleanbase.exception.base.GenericException;

public interface BaseUseCase<I extends ReadModel, O> {
    O action(I readModel) throws GenericException;
}