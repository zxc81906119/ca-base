package com.redhat.cleanbase.ca.usecase;

import com.redhat.cleanbase.ca.readmodel.AirwavesReadModel;
import org.springframework.stereotype.Service;

@Service
public class AirwavesService extends AirwavesUseCase {
    @Override
    protected String process(AirwavesReadModel readModel) {
        return null;
    }
}
