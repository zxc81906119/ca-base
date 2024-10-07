package com.redhat.cleanbase.security.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeProperties {
    private Long time;
    private TimeUnit timeUnit;
}