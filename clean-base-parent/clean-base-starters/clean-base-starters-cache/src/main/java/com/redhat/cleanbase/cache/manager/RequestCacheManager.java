package com.redhat.cleanbase.cache.manager;

import org.springframework.cache.CacheManager;

public interface RequestCacheManager extends CacheManager {

    void clearRequestCaches();

}
