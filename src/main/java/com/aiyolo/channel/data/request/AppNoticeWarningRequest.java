package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

public class AppNoticeWarningRequest extends AppRequest {

    public static final String ACTION = "notice_warning";

    private static AppNoticeWarningRequest instance;

    public static AppNoticeWarningRequest getInstance() {
        if (instance == null) {
            instance = new AppNoticeWarningRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("action", ACTION);
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("message", generateMessageId());
            bodyMap.put("title", data.get("title"));
            bodyMap.put("text", data.get("text"));
            bodyMap.put("alarm_type", data.get("alarm_type"));
            bodyMap.put("net_status", data.get("net_status"));
            bodyMap.put("dev_status", data.get("dev_status"));
            bodyMap.put("timestamp", System.currentTimeMillis());
            bodyMap.put("temp", data.get("temp"));
            bodyMap.put("hum", data.get("hum"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeWarningRequestBody异常！", e);
        }
        return null;
    }

}
