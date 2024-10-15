package com.redhat.cleanbase.cache.manager.condition;

import com.redhat.cleanbase.common.type.Condition;
import org.springframework.cache.CacheManager;

public interface CacheManagerCondition extends Condition<String>, CacheManager {
    default boolean isSupported(String t) {
        return t.startsWith(getCacheNamePrefix());
    }

    String getCacheNamePrefix();
}
