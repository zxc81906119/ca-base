package com.redhat.cleanbase.exception.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TryActionResult<D> {
    private D data;
    private Exception exception;

    public boolean isSuccess() {
        return !hasException();
    }

    public boolean hasException() {
        return exception != null;
    }

    public boolean hasData() {
        return data != null;
    }

    public List<Throwable> getCauseList() {
        val throwableList = new ArrayList<Throwable>();
        if (hasException()) {
            Throwable pointer = exception;
            while ((pointer = pointer.getCause()) != null) {
                throwableList.add(pointer);
            }
        }
        return throwableList;
    }

    public void orThrowsSelf() throws Exception {
        if (hasException()) {
            throw exception;
        }
    }

    public void orThrowsRuntimeException() {
        if (hasException()) {
            throw exception instanceof RuntimeException runtimeException
                    ? runtimeException
                    : new RuntimeException(exception);
        }
    }

    public <E extends Exception> void orThrowsFirst(Class<E> exceptionClazz) throws E {
        if (!hasException()) {
            return;
        }
        orThrowsSelf(exceptionClazz);
        val optionalE = getRelationExceptions().stream()
                .filter(exceptionClazz::isInstance)
                .map(exceptionClazz::cast)
                .findFirst();
        if (optionalE.isPresent()) {
            throw optionalE.get();
        }
    }

    public <E extends Exception> void orThrowsSelf(Class<E> exceptionClazz) throws E {
        if (exceptionClazz.isInstance(exception)) {
            throw exceptionClazz.cast(exception);
        }
    }

    public List<Exception> getRelationExceptions() {
        if (!hasException()) {
            return List.of();
        }
        val throwableList = new ArrayList<>(getSuppressedList());
        throwableList.addAll(getCauseList());
        return throwableList.stream()
                .filter(Exception.class::isInstance)
                .map(Exception.class::cast)
                .toList();
    }

    public List<Throwable> getSuppressedList() {
        return Optional.ofNullable(exception)
                .map(Throwable::getSuppressed)
                .map(Arrays::asList)
                .orElseGet(List::of);
    }
}
