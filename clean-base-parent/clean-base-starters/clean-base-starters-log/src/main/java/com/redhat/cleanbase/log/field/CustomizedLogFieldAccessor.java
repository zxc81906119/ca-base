package com.redhat.cleanbase.log.field;

import com.redhat.cleanbase.common.type.IdentifiableGetter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.List;

@Slf4j
public class CustomizedLogFieldAccessor extends IdentifiableGetter<String, CustomizedLogField> {


    public CustomizedLogFieldAccessor(List<CustomizedLogField> identifiableList) {
        super(identifiableList);
    }

    public void set(CustomizedLogField customizedLogField, String value) {
        set(customizedLogField.name(), value);
    }

    public void set(String name, String value) {
        getIdentifiableOpt(name)
                .orElseThrow(() -> new RuntimeException("不存在客製化欄位 : %s".formatted(name)));

        log.info("set customized log field: {} , value: {} ", name, value);

        MDC.put(name, value);
    }

}