package com.aiyolo.channel.data.response;

import com.aiyolo.constant.AlarmTemperatureConsts;
import com.aiyolo.entity.GatewaySetting;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GatewayPushResponse extends GatewayResponse {

    public static final String ACTION = "upsta";

    private static GatewayPushResponse instance;

    public static GatewayPushResponse getInstance() {
        if (instance == null) {
            instance = new GatewayPushResponse();
        }
        return instance;
    }

    @Override
    public Map<String, Object> responseBody(JSONObject request, Object data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", request.getJSONObject("body").get("act"));
            bodyMap.put("pid", request.getJSONObject("body").get("pid"));
            bodyMap.put("imei", request.getJSONObject("body").get("imei"));
            bodyMap.put("pin", request.getJSONObject("body").get("pin"));
            bodyMap.put("mid", request.getJSONObject("body").get("mid"));
            bodyMap.put("ret", 0);

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayUpstaResponseBody异常！", e);
        }
        return null;
    }

}
