package com.aiyolo.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class GatewayBeatCache {

    /**
     * 网关心跳缓存
     *
     * @param String glId
     * @return 网关最后心跳时间（毫秒）
     */
    @Cacheable("gatewayBeat")
    public long getByGlId(String glId) {
        return 0L;
    }

    @CachePut(value = "gatewayBeat", key = "#p0")
    public long save(String glId, long timestamp) {
        return timestamp;
    }

}
