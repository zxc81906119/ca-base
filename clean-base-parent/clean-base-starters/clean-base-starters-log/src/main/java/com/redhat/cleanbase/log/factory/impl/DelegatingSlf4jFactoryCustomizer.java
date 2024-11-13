package com.redhat.cleanbase.log.factory.impl;

import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.log.factory.Slf4jFactoryCustomizeCondition;
import com.redhat.cleanbase.log.factory.Slf4jFactoryCustomizer;
import com.redhat.cleanbase.log.field.CustomizedLogField;
import lombok.NonNull;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class DelegatingSlf4jFactoryCustomizer extends ConditionSelector<ILoggerFactory, Slf4jFactoryCustomizeCondition> implements Slf4jFactoryCustomizer {

    public DelegatingSlf4jFactoryCustomizer(@NonNull Collection<Slf4jFactoryCustomizeCondition> conditions, List<CustomizedLogField> customizedLogFields) {
        super(conditions);
        customize(LoggerFactory.getILoggerFactory(), customizedLogFields);
    }

    @Override
    public void customize(ILoggerFactory iLoggerFactory, List<CustomizedLogField> customizedLogFields) {
        getFirstCondition(iLoggerFactory)
                .ifPresent((factoryCondition) -> factoryCondition.customize(iLoggerFactory, customizedLogFields));
    }
}
