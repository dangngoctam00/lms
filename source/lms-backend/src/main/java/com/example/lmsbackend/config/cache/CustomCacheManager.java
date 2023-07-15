package com.example.lmsbackend.config.cache;

import com.example.lmsbackend.multitenancy.utils.TenantContext;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class CustomCacheManager extends RedisCacheManager {
    public CustomCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        RedisCacheManager.builder()
                .cacheWriter(cacheWriter)
                .cacheDefaults(defaultCacheConfiguration)
                .build();
    }
    @Override
    public Cache getCache(String name) {
        if (TenantContext.getTenantId().isEmpty()) return super.getCache(name);
        return super.getCache(TenantContext.getTenantId() + ":" + name);
    }
}
