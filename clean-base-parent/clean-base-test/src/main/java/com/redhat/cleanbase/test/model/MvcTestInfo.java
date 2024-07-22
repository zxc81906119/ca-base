package com.redhat.cleanbase.test.model;

import com.redhat.cleanbase.test.annotation.MvcInfo;
import lombok.NonNull;

public record MvcTestInfo(
        String methodName
        , String simpleClassName
        , @NonNull MvcInfo mvcInfo
) {
}
