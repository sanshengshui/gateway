package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.DeviceIOSetCache;
import com.aiyolo.common.SpringUtil;

public class DeviceIosetProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            init(message);

            String imei = messageBodyJson.getString("imei");
            int result = messageBodyJson.getInt("res");

            DeviceIOSetCache deviceIOSetCache = (DeviceIOSetCache) SpringUtil.getBean("deviceIOSetCache");

            deviceIOSetCache.save(imei, result);
        } catch (Exception e) {
            errorLogger.error("DeviceIosetProcessor异常！message:" + message, e);
        }
    }

}
