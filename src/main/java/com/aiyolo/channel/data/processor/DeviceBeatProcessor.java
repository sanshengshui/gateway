package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.DeviceBeatCache;
import com.aiyolo.common.SpringUtil;

public class DeviceBeatProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            init(message);

            String glId = messageHeaderJson.getString("gl_id");
            long timestamp = messageHeaderJson.getLong("timestamp");

            DeviceBeatCache deviceBeatCache = (DeviceBeatCache) SpringUtil.getBean("deviceBeatCache");

            deviceBeatCache.save(glId, timestamp);
        } catch (Exception e) {
            errorLogger.error("DeviceBeatProcessor异常！message:" + message, e);
        }
    }

}
