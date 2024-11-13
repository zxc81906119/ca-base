package com.redhat.cleanbase.log.factory;

import com.redhat.cleanbase.common.type.Condition;
import org.slf4j.ILoggerFactory;

public interface Slf4jFactoryCustomizeCondition extends Condition<ILoggerFactory>, Slf4jFactoryCustomizer {
}
