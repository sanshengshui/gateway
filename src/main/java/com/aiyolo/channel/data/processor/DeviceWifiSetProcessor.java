package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.DeviceWifiSetCache;
import com.aiyolo.common.SpringUtil;

public class DeviceWifiSetProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            init(message);

            String imei = messageBodyJson.getString("imei");
            int result = messageBodyJson.getInt("res");

            DeviceWifiSetCache deviceWifiSetCache = (DeviceWifiSetCache) SpringUtil.getBean("deviceWifiSetCache");

            deviceWifiSetCache.save(imei, result);
        } catch (Exception e) {
            errorLogger.error("DeviceWifiSetProcessor异常！message:" + message, e);
        }
    }

}
