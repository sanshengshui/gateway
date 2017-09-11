package com.aiyolo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DeviceLiveStatusCache {

    public static final int UNKNOWN = -1;
    public static final int DEAD = 0;
    public static final int LIVE = 1;

    @Autowired
    CommonCache commonCache;

    @Value("${device.live.beat.period}")
    private long deviceLiveBeatPeriod;

    @Cacheable(value="deviceLiveStatus", unless="#result == T(com.aiyolo.cache.DeviceLiveStatusCache).UNKNOWN")
    public int getByGlId(String glId) {
        long currentTime = System.currentTimeMillis();
        long appLaunchTime = (long) commonCache.get("appLaunchTime");
        if (currentTime - appLaunchTime > deviceLiveBeatPeriod) {
            return DEAD;
        }

        return UNKNOWN;
    }

    @CachePut(value = "deviceLiveStatus", key = "#p0")
    public int save(String glId, int liveStatus) {
        return liveStatus;
    }

}
