package com.redhat.cleanbase.log.factory;

import com.redhat.cleanbase.log.field.CustomizedLogField;
import org.slf4j.ILoggerFactory;

import java.util.List;

public interface Slf4jFactoryCustomizer {
    void customize(ILoggerFactory iLoggerFactory, List<CustomizedLogField> customizedLogFields);
}
