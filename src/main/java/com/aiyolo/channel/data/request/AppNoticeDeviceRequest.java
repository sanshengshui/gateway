package com.aiyolo.channel.data.request;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class AppNoticeDeviceRequest extends AppRequest {

    public static final String ACTION = "notice_device";

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
            bodyMap.put("imeiGateway", data.get("imeiGateway"));
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("notice", data.get("notice"));
            bodyMap.put("dev", data.get("dev"));
            bodyMap.put("pid", data.get("pid"));
            bodyMap.put("online", MapUtils.getInteger(data, "online", 0));
            bodyMap.put("position", MapUtils.getString(data, "position", ""));
            bodyMap.put("name", MapUtils.getString(data, "name", ""));
            bodyMap.put("rssi", MapUtils.getInteger(data, "rssi", 0));
            bodyMap.put("val", MapUtils.getInteger(data, "val", 0));
            bodyMap.put("bat", MapUtils.getInteger(data, "bat", 0));
            bodyMap.put("check", MapUtils.getInteger(data, "check", 0));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeDeviceRequestBody异常！", e);
        }
        return null;
    }

}
