package com.aiyolo.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class GatewayBeatCache {

    /**
     * 网关心跳缓存
     *
     * @param String glImei
     * @return 网关最后心跳时间（毫秒）
     */
    @Cacheable("gatewayBeat")
    public long getByGlImei(String glImei) {
        return 0L;
    }

    @CachePut(value = "gatewayBeat", key = "#p0")
    public long save(String glImei, long timestamp) {
        return timestamp;
    }

}
