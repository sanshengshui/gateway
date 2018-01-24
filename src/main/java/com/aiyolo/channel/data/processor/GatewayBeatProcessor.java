package com.aiyolo.channel.data.processor;

import com.aiyolo.cache.GatewayBeatCache;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.ProtocolFieldConsts;

public class GatewayBeatProcessor extends Processor {

    @Override
    public void run(String message) {
        try {
            init(message);

            String glImei = messageHeaderJson.getString(ProtocolFieldConsts.IMEI);
            long timestamp = messageHeaderJson.getLong(ProtocolFieldConsts.TIMESTAMP);

            GatewayBeatCache gatewayBeatCache = (GatewayBeatCache) SpringUtil.getBean("gatewayBeatCache");

            gatewayBeatCache.save(glImei, timestamp);
        } catch (Exception e) {
            errorLogger.error("GatewayBeatProcessor异常！message:" + message, e);
        }
    }

}
