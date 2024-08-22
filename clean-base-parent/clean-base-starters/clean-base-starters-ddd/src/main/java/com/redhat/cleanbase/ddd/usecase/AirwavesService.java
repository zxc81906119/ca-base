package com.redhat.cleanbase.ddd.usecase;

import com.redhat.cleanbase.ddd.readmodel.AirwavesReadModel;
import org.springframework.stereotype.Service;

@Service
public class AirwavesService extends AirwavesUseCase {
    @Override
    protected String process(AirwavesReadModel readModel) {
        return null;
    }
}
