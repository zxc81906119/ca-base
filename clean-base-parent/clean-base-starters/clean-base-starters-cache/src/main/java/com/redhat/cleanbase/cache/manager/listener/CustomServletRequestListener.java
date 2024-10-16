package com.redhat.cleanbase.cache.manager.listener;

import com.redhat.cleanbase.cache.manager.RequestCacheManager;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomServletRequestListener implements ServletRequestListener {
    @NonNull
    private final RequestCacheManager manager;

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        manager.clearRequestCaches();
    }
}