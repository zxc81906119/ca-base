package com.redhat.cleanbase.cache.manager.request;

import org.springframework.cache.CacheManager;

public interface RequestCacheManager extends CacheManager {

    void clearRequestCaches();

}
