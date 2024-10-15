package com.redhat.cleanbase.cache.manager.getter;

import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.function.Supplier;

public interface CacheManagersGetter extends Supplier<List<CacheManager>> {
}
