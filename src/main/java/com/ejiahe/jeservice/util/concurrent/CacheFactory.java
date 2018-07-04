package com.ejiahe.jeservice.util.concurrent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheFactory {

    private static Map<String,Cache> localCache = new ConcurrentHashMap<String,Cache>();


    public static synchronized <T extends Cache> T createLocalCache(String name, long expireAfterAccess, int maximumSize){
        T cache = (T)localCache.get(name);

        if(cache != null){
            return cache;
        }

        if(maximumSize < 0){
            maximumSize = Integer.MAX_VALUE;
        }

        cache = (T)CacheBuilder.newBuilder().expireAfterAccess(expireAfterAccess, TimeUnit.MILLISECONDS)
                .maximumSize(maximumSize).build();

        localCache.put(name,cache);

        return cache;
    }

}
