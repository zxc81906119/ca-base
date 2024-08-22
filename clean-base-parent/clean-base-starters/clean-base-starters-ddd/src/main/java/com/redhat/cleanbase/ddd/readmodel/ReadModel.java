package com.redhat.cleanbase.ddd.readmodel;

import com.redhat.cleanbase.exception.base.GenericException;

public interface ReadModel {
    void validate() throws GenericException;
}