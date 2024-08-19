package com.redhat.cleanbase.exception.content;

import com.redhat.cleanbase.code.response.ResponseCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public final class GenericExceptionContent {

    private static final String VIOLATIONS = "violations";

    private final Map<String, Object> details = new HashMap<>();
    @Getter
    @NonNull
    private final ResponseCode code;

    public void setViolations(Map<String, List<String>> violations) {
        putDetail(VIOLATIONS, violations);
    }

    public void putDetail(@NonNull String key, Object value) {
        if (value != null) {
            details.put(key, value);
        }
    }

    public Map<String, Object> getDetails() {
        return new HashMap<>(details);
    }

}