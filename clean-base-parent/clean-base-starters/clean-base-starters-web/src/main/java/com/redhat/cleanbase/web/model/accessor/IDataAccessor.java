package com.redhat.cleanbase.web.model.accessor;

public interface IDataAccessor<T> {
    T getData();

    void setData(T t);
}
