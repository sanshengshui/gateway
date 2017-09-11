package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

public class DeviceWifiSetRequest extends DeviceRequest {

    public static final String ACTION = "wifi_set";

    private static DeviceWifiSetRequest instance;

    public static DeviceWifiSetRequest getInstance() {
        if (instance == null) {
            instance = new DeviceWifiSetRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("pid", data.get("pid"));
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("ssid", data.get("ssid"));
            bodyMap.put("pwd", data.get("pwd"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("DeviceWifiSetRequestBody异常！", e);
        }
        return null;
    }

}
