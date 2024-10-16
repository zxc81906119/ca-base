package com.redhat.cleanbase.exception.info;

import com.redhat.cleanbase.code.response.ResponseCode;
import lombok.NonNull;

public record ExceptionInfo(
        @NonNull ResponseCode code,
        String title,
        String example,
        String desc
) {
}