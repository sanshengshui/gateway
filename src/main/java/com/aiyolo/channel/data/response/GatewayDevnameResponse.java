package com.aiyolo.channel.data.response;

import com.aiyolo.entity.DeviceCategory;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PID;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

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
            Map<String, Object> bodyMap = new LinkedHashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put(PID, request.getJSONObject("body").get(PID));
            bodyMap.put(IMEI, request.getJSONObject("body").get(IMEI));
            bodyMap.put(PIN, request.getJSONObject("body").get(PIN));
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
