package com.toker.sys.utils.tools;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class CacheUtil {
    private static CacheUtil _instance;
    private Cache<String, String> deviceCache;

    private CacheUtil() {
        deviceCache = CacheBuilder.newBuilder().initialCapacity(5).maximumSize(500).build();
    }

    public synchronized static CacheUtil getInstance() {
        if (null == _instance) {
            _instance = new CacheUtil();
        }
        return _instance;
    }

    public void put(String address, String device) {
        deviceCache.put(address, device);
    }

    public String get(String address) {
        return deviceCache.getIfPresent(address);
    }
}
