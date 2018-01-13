package com.aiyolo.channel.data.request;

import org.apache.commons.collections.MapUtils;

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
            bodyMap.put("dev_num", MapUtils.getInteger(data, "dev_num", 0));
            bodyMap.put("err", MapUtils.getInteger(data, "err", 0));
            bodyMap.put("online", MapUtils.getInteger(data, "online", 0));
            bodyMap.put("temp", MapUtils.getInteger(data, "temp", 0));
            bodyMap.put("hum", MapUtils.getInteger(data, "hum", 0));
            bodyMap.put("atm", MapUtils.getInteger(data, "atm", 0));
            bodyMap.put("ver", MapUtils.getString(data, "ver", ""));
            bodyMap.put("check", MapUtils.getInteger(data, "check", 0));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("AppNoticeGatewayRequestBody异常！", e);
        }
        return null;
    }

}
