package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;

import java.util.HashMap;
import java.util.Map;

public class GatewayBroaRequest extends GatewayRequest {

    public static final String ACTION = "broa";

    private static GatewayBroaRequest instance;

    public static GatewayBroaRequest getInstance() {
        if (instance == null) {
            instance = new GatewayBroaRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("pid", ChannelConsts.PRODUCT_ID);
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("pin", data.get("pin"));
            bodyMap.put("mid", System.currentTimeMillis() / 1000);
            bodyMap.put("binfo", data.get("binfo"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayBroaRequest异常！", e);
        }
        return null;
    }

}
