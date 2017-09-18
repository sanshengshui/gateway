package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;

import java.util.HashMap;
import java.util.Map;

public class GatewayPairRequest extends GatewayRequest {

    public static final String ACTION = "pair";

    private static GatewayPairRequest instance;

    public static GatewayPairRequest getInstance() {
        if (instance == null) {
            instance = new GatewayPairRequest();
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

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayPairRequest异常！", e);
        }
        return null;
    }

}
