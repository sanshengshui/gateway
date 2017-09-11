package com.aiyolo.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CommonCache {

    @Cacheable("common")
    public Object get(String key) {
        return null;
    }

    @CachePut(value = "common", key = "#p0")
    public Object save(String key, Object value) {
        return value;
    }

}
