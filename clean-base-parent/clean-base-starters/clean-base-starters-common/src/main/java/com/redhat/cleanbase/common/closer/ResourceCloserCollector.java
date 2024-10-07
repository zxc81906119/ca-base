package com.redhat.cleanbase.common.closer;

import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class ResourceCloserCollector implements AutoCloseable {
    private static final ThreadLocal<ResourceCloserCollector> RESOURCE_CLOSER_COLLECTOR_THREAD_LOCAL = new ThreadLocal<>();

    private final List<ResourceCloser> resourceClosers = new ArrayList<>();

    public static ResourceCloserCollector getClearInstance() {
        return getInstance(true);
    }

    public static ResourceCloserCollector getCurrentInstance() {
        return getInstance(false);
    }

    private static void clear() {
        RESOURCE_CLOSER_COLLECTOR_THREAD_LOCAL.remove();
    }

    public static ResourceCloserCollector getInstance(boolean needClear) {
        if (needClear) {
            clear();
        }

        ResourceCloserCollector resourceCloserCollector = RESOURCE_CLOSER_COLLECTOR_THREAD_LOCAL.get();
        if (resourceCloserCollector == null) {
            RESOURCE_CLOSER_COLLECTOR_THREAD_LOCAL.set(resourceCloserCollector = new ResourceCloserCollector());
        }
        return resourceCloserCollector;
    }

    public void addCloseable(String resourceName, AutoCloseable closeable) throws Exception {
        addCloseable(resourceName, false, false, closeable);
    }

    public void addCloseable(AutoCloseable closeable) throws Exception {
        addCloseable(null, false, false, closeable);
    }

    public void addCloseable(String resourceName, boolean immediately, boolean isFailThrow, AutoCloseable closeable) throws Exception {
        val resourceCloser = new ResourceCloser(resourceName, closeable, isFailThrow);
        if (immediately) {
            resourceCloser.close();
            return;
        }
        resourceClosers.add(resourceCloser);
    }

    @Override
    public void close() throws Exception {
        try {
            for (ResourceCloser resourceCloser : resourceClosers) {
                resourceCloser.close();
            }
        } finally {
            resourceClosers.clear();
            clear();
        }
    }
}