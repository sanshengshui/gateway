package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

public class AppNoticeDeviceRequest extends AppRequest {

    public static final String ACTION = "notice_dev";

    private static AppNoticeDeviceRequest instance;

    public static AppNoticeDeviceRequest getInstance() {
        if (instance == null) {
            instance = new AppNoticeDeviceRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("notice", data.get("notice"));
            bodyMap.put("dev", data.get("dev"));
            bodyMap.put("pid", data.get("pid"));
            bodyMap.put("online", data.get("online"));
            bodyMap.put("location", data.get("location"));
            bodyMap.put("name", data.get("name"));
            bodyMap.put("rssi", data.get("rssi"));
            bodyMap.put("val", data.get("val"));
            bodyMap.put("bat", data.get("bat"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeDeviceRequestBody异常！", e);
        }
        return null;
    }

}
