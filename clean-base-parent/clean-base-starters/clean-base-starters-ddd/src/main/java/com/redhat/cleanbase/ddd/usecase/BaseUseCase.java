package com.redhat.cleanbase.ddd.usecase;

import com.redhat.cleanbase.ddd.readmodel.ReadModel;
import com.redhat.cleanbase.exception.base.GenericException;

public interface BaseUseCase<I extends ReadModel, O> {
    O action(I readModel) throws GenericException;
}