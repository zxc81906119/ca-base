package com.redhat.cleanbase.cache.consts;

public final class CacheConstants {
    public static final String LOCAL_CACHE_PREFIX = "local:";

    private CacheConstants() {
        throw new UnsupportedOperationException();
    }


    public static final class CacheName {
        public static final String LOCAL_TEN_SEC = LOCAL_CACHE_PREFIX + "10s";
        public static final String LOCAL_ONE_MIN = LOCAL_CACHE_PREFIX + "1min";
        public static final String LOCAL_FIVE_MIN = LOCAL_CACHE_PREFIX + "5min";
        public static final String LOCAL_TEN_MIN = LOCAL_CACHE_PREFIX + "10min";
        public static final String LOCAL_ONE_HOUR = LOCAL_CACHE_PREFIX + "1hr";
        public static final String LOCAL_ONE_DAY = LOCAL_CACHE_PREFIX + "1day";
        private CacheName() {
            throw new UnsupportedOperationException();
        }
    }
}
