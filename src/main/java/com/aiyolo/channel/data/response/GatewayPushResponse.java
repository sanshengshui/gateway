package com.aiyolo.channel.data.response;

import com.aiyolo.constant.AlarmTemperatureConsts;
import com.aiyolo.entity.GatewaySetting;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PID;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

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
            Map<String, Object> bodyMap = new LinkedHashMap<>();

            bodyMap.put("act", request.getJSONObject("body").get("act"));
            bodyMap.put(PID, request.getJSONObject("body").get(PID));
            bodyMap.put(IMEI, request.getJSONObject("body").get(IMEI));
            bodyMap.put(PIN, request.getJSONObject("body").get(PIN));
            bodyMap.put("mid", request.getJSONObject("body").get("mid"));
            bodyMap.put("ret", 0);

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayUpstaResponseBody异常！", e);
        }
        return null;
    }

}
