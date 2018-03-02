package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;

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

            bodyMap.put("act", ACTION);
            bodyMap.put("imeiGateway", data.get("imeiGateway"));
            bodyMap.put(IMEI, data.get(IMEI));
            bodyMap.put("title", data.get("title"));
            bodyMap.put("text", data.get("text"));
            bodyMap.put("timestamp", data.get("timestamp"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeWarningRequestBody异常！", e);
        }
        return null;
    }

}
