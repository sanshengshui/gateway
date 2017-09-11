package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.constant.ChannelConsts;

public class DeviceIOSetRequest extends DeviceRequest {

    public static final String ACTION = "ioset";

    private static DeviceIOSetRequest instance;

    public static DeviceIOSetRequest getInstance() {
        if (instance == null) {
            instance = new DeviceIOSetRequest();
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
            if (data.get("voi") != null) {
                bodyMap.put("voi", data.get("voi"));
            }
            if (data.get("swi") != null) {
                bodyMap.put("swi", data.get("swi"));
            }

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("DeviceIOSetRequestBody异常！", e);
        }
        return null;
    }

}
