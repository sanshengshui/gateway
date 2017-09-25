package com.aiyolo.channel.data.response;

import com.aiyolo.entity.DeviceCategory;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GatewayDevnameResponse extends GatewayResponse {

    public static final String ACTION = "devname";

    private static GatewayDevnameResponse instance;

    public static GatewayDevnameResponse getInstance() {
        if (instance == null) {
            instance = new GatewayDevnameResponse();
        }
        return instance;
    }

    @Override
    public Map<String, Object> responseBody(JSONObject request, Object data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("pid", request.getJSONObject("body").get("pid"));
            bodyMap.put("imei", request.getJSONObject("body").get("imei"));
            bodyMap.put("pin", request.getJSONObject("body").get("pin"));
            bodyMap.put("mid", request.getJSONObject("body").get("mid"));
            bodyMap.put("cat", request.getJSONObject("body").get("cat"));
            bodyMap.put("type", request.getJSONObject("body").get("type"));

            if (data != null) {
                DeviceCategory deviceCategory = (DeviceCategory) data;
                bodyMap.put("name", deviceCategory.getCode());
                bodyMap.put("ret", 0);
            } else {
                bodyMap.put("name", "");
                bodyMap.put("ret", -1);
            }

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayDevnameResponse异常！", e);
        }
        return null;
    }

}
