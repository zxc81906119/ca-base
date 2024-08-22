package com.redhat.cleanbase.ca.usecase;

import com.redhat.cleanbase.ca.readmodel.ReadModel;
import com.redhat.cleanbase.exception.base.GenericException;
import lombok.NonNull;

public abstract class UseCase<I extends ReadModel, O> implements BaseUseCase<I, O> {

    @Override
    public O action(@NonNull I readModel) throws GenericException {
        readModel.validate();
        return process(readModel);
    }

    protected abstract O process(I readModel) throws GenericException;
}