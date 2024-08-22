package com.redhat.cleanbase.ddd.readmodel;

import com.redhat.cleanbase.ddd.readmodel.exception.FlavorNotRightException;
import lombok.val;

public record AirwavesReadModel(String flavor) implements ReadModel {
    @Override
    public void validate() throws FlavorNotRightException {
        val equals = "超涼薄荷".equals(flavor);
        if (!equals) {
            throw new FlavorNotRightException();
        }
    }
}