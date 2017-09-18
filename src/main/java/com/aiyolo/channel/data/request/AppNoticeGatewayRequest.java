package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

public class AppNoticeGatewayRequest extends AppRequest {

    public static final String ACTION = "notice_gateway";

    private static AppNoticeGatewayRequest instance;

    public static AppNoticeGatewayRequest getInstance() {
        if (instance == null) {
            instance = new AppNoticeGatewayRequest();
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
            bodyMap.put("dev_num", data.get("dev_num"));
            bodyMap.put("err", data.get("err"));
            bodyMap.put("online", data.get("online"));
            bodyMap.put("temp", data.get("temp"));
            bodyMap.put("hum", data.get("hum"));
            bodyMap.put("atm", data.get("atm"));
            bodyMap.put("ver", data.get("ver"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeGatewayRequestBody异常！", e);
        }
        return null;
    }

}
