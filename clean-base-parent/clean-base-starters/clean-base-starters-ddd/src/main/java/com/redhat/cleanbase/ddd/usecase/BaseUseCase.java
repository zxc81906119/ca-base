package com.redhat.cleanbase.ddd.usecase;

import com.redhat.cleanbase.ddd.readmodel.ReadModel;

public interface BaseUseCase<I extends ReadModel, O> {
    O action(I readModel) throws Exception;
}