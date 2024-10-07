package com.redhat.cleanbase.common.func;

@FunctionalInterface
public interface BiOperator<I1, I2, O, E extends Exception> {
    O operate(I1 i, I2 j) throws E;
}