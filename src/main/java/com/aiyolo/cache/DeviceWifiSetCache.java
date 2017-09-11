package com.aiyolo.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DeviceWifiSetCache {

    public static final int RESULT_EMPTY = -1;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILED = 1;

    @Cacheable("deviceWifiSet")
    public int getByImei(String imei) {
        return RESULT_EMPTY;
    }

    @CachePut(value = "deviceWifiSet", key = "#p0")
    public int save(String imei, int result) {
        return result;
    }

}
