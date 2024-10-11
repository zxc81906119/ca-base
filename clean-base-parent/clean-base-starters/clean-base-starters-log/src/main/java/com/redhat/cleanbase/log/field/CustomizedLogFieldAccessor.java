package com.redhat.cleanbase.log.field;

import com.redhat.cleanbase.common.type.IdentifiableGetter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;

import java.util.List;

@Slf4j
public class CustomizedLogFieldAccessor<T extends CustomizedLogField> extends IdentifiableGetter<String, T> {


    public CustomizedLogFieldAccessor(List<T> identifiableList) {
        super(identifiableList);
    }

    public void set(T customizedLogField, String value) {
        set(customizedLogField.name(), value);
    }

    public void set(String name, String value) {
        val identifiable = getIdentifiable(name);
        if (identifiable == null) {
            log.warn("logField : {} 非由本處理器管轄", name);
        }
        log.info("set mdc name: {} , value: {} ", name, value);
        MDC.put(name, value);
    }

}