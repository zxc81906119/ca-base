package com.redhat.cleanbase.cache.manager.condition.impl;

import com.redhat.cleanbase.cache.manager.RequestCacheManager;
import com.redhat.cleanbase.cache.manager.condition.RequestCacheManagerCondition;
import lombok.NonNull;

public class DefaultRequestCacheManagerCondition extends RequestCacheManagerCondition {

    public DefaultRequestCacheManagerCondition(@NonNull RequestCacheManager requestCacheManager) {
        super(requestCacheManager);
    }

}