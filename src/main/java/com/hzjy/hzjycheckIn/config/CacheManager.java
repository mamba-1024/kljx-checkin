package com.hzjy.hzjycheckIn.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheManager {

    private Cache<String, Integer> tHcache;

//    private CacheManager<String, List>

    public CacheManager() {
        tHcache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(24, TimeUnit.HOURS).build();
    }


    public void put(String key, Integer value) {
        tHcache.put(key, value);
    }

    public Integer get(String key) {
        return tHcache.getIfPresent(key);
    }

    public void remove(String key) {
        tHcache.invalidate(key);
    }

    public void removeAll() {
        tHcache.invalidateAll();
    }

    public void clearCache() {
        tHcache.invalidateAll();
    }

}
