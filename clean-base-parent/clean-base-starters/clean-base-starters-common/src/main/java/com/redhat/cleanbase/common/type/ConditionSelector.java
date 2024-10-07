package com.redhat.cleanbase.common.type;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class ConditionSelector<I, C extends Condition<I>> {

    @NonNull
    private final Collection<C> conditions;

    public Optional<C> getFirstCondition(I i) {
        return getConditionStream(i)
                .findFirst();
    }

    public List<C> getConditions(I i) {
        return getConditionStream(i)
                .collect(Collectors.toList());
    }

    public Stream<C> getConditionStream(I i) {
        return conditions.stream()
                .filter(Objects::nonNull)
                .filter((condition) -> condition.isSupported(i));
    }

}
