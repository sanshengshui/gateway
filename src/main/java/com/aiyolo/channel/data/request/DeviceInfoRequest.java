package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.constant.ChannelConsts;

public class DeviceInfoRequest extends DeviceRequest {

    private static DeviceInfoRequest instance;

    public static DeviceInfoRequest getInstance() {
        if (instance == null) {
            instance = new DeviceInfoRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestHeader(String[] glIds) {
        Map<String, Object> headerMap = super.requestHeader(glIds);
        if (headerMap != null) {
            headerMap.remove("code");
            headerMap.put("cmd", "info");
            headerMap.put("product_id", ChannelConsts.PRODUCT_ID);
        }
        return headerMap;
    }

    public Map<String, Object> requestBody(String glId) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("glId", glId);
        return requestBody(data);
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("gl_id", data.get("glId"));

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("DeviceInfoRequestBody异常！", e);
        }
        return null;
    }

}
