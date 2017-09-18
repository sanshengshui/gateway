package com.aiyolo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class GatewayLiveStatusCache {

    public static final int UNKNOWN = -1;
    public static final int DEAD = 0;
    public static final int LIVE = 1;

    @Autowired
    CommonCache commonCache;

    @Value("${gateway.live.beat.period}")
    private long gatewayLiveBeatPeriod;

    @Cacheable(value="gatewayLiveStatus", unless="#result == T(com.aiyolo.cache.GatewayLiveStatusCache).UNKNOWN")
    public int getByGlId(String glId) {
        long currentTime = System.currentTimeMillis();
        long appLaunchTime = (long) commonCache.get("appLaunchTime");
        if (currentTime - appLaunchTime > gatewayLiveBeatPeriod) {
            return DEAD;
        }

        return UNKNOWN;
    }

    @CachePut(value = "gatewayLiveStatus", key = "#p0")
    public int save(String glId, int liveStatus) {
        return liveStatus;
    }

}
