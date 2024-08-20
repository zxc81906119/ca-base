package com.redhat.cleanbase.ca.usecase;

import com.redhat.cleanbase.ca.readmodel.ReadModel;
import com.redhat.cleanbase.exception.base.GenericException;
import lombok.NonNull;

public abstract class UseCase<R extends ReadModel, T> implements BaseUseCase<R, T> {

    @Override
    public T action(@NonNull R readModel) throws GenericException {
        readModel.validate();
        return process(readModel);
    }

    protected abstract T process(R readModel) throws GenericException;
}