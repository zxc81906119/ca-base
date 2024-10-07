package com.redhat.cleanbase.common.func;

@FunctionalInterface
public interface Operator<I, O, E extends Exception> {
    O operate(I i) throws E;
}