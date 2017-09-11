package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.constant.ChannelConsts;

public class AppMessagePushRequest extends AppRequest {

    private static AppMessagePushRequest instance;

    public static AppMessagePushRequest getInstance() {
        if (instance == null) {
            instance = new AppMessagePushRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestHeader(String[] mobileIds) {
        Map<String, Object> headerMap = super.requestHeader(mobileIds);
        if (headerMap != null) {
            headerMap.remove("code");
            headerMap.put("cmd", "push_text");
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
            errorLogger.error("AppMessagePushRequestBody异常！", e);
        }
        return null;
    }

}
