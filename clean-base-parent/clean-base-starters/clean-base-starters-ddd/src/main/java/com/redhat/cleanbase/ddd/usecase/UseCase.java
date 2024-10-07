package com.redhat.cleanbase.ddd.usecase;

import com.redhat.cleanbase.common.closer.ResourceCloserCollector;
import com.redhat.cleanbase.ddd.readmodel.ReadModel;
import lombok.NonNull;
import lombok.val;


public abstract class UseCase<I extends ReadModel, O> implements BaseUseCase<I, O> {

    @Override
    public final O action(@NonNull I readModel) throws Exception {
        val closerCollector = ResourceCloserCollector.getClearInstance();
        try (closerCollector) {
            readModel.validate();
            return process(readModel);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    protected O handleException(Exception e) throws Exception {
        throw e;
    }

    protected abstract O process(I readModel) throws Exception;
}