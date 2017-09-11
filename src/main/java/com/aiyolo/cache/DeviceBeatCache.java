package com.aiyolo.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DeviceBeatCache {

    /**
     * 设备心跳缓存
     *
     * @param String glId
     * @return 设备最后心跳时间（毫秒）
     */
    @Cacheable("deviceBeat")
    public long getByGlId(String glId) {
        return 0L;
    }

    @CachePut(value = "deviceBeat", key = "#p0")
    public long save(String glId, long timestamp) {
        return timestamp;
    }

}
