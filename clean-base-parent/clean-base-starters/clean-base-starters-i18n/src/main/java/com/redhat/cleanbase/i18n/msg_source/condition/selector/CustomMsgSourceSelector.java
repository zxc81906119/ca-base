package com.redhat.cleanbase.i18n.msg_source.condition.selector;

import com.redhat.cleanbase.common.type.ConditionSelector;
import com.redhat.cleanbase.i18n.msg_source.base.CustomMsgSource;
import com.redhat.cleanbase.i18n.msg_source.input.I18nInput;

import java.util.Collection;

public class CustomMsgSourceSelector extends ConditionSelector<Class<? extends I18nInput>, CustomMsgSource> {

    public CustomMsgSourceSelector(Collection<CustomMsgSource> conditions) {
        super(conditions);
    }
}
