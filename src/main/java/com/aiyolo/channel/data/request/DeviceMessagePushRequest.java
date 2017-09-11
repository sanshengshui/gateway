package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.constant.ChannelConsts;

public class DeviceMessagePushRequest extends DeviceRequest {

    private static DeviceMessagePushRequest instance;

    public static DeviceMessagePushRequest getInstance() {
        if (instance == null) {
            instance = new DeviceMessagePushRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestHeader(String[] glIds) {
        Map<String, Object> headerMap = super.requestHeader(glIds);
        if (headerMap != null) {
            headerMap.remove("code");
            headerMap.put("cmd", "push");
            headerMap.put("product_id", ChannelConsts.PRODUCT_ID);
        }
        return headerMap;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("gl_id", data.get("glId"));
            bodyMap.put("title", data.get("title"));
            bodyMap.put("text", data.get("text"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("DeviceMessagePushRequestBody异常！", e);
        }
        return null;
    }

}
