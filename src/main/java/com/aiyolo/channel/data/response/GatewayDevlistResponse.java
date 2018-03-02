package com.aiyolo.channel.data.response;

import com.aiyolo.entity.Device;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.PID;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

public class GatewayDevlistResponse extends GatewayResponse {

    public static final String ACTION = "devlist";

    private static GatewayDevlistResponse instance;

    public static GatewayDevlistResponse getInstance() {
        if (instance == null) {
            instance = new GatewayDevlistResponse();
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
            bodyMap.put("ret", 0);

            List<Map> devs = new ArrayList<Map>();
            if (data != null) {
                List<Device> devices = (List<Device>) data;
                for (int i = 0; i < devices.size(); i++) {
                    Map<String, String> dev = new HashMap<String, String>();
                    dev.put("dev", devices.get(i).getType());
                    dev.put(PID, devices.get(i).getPid());
                    dev.put(IMEI, devices.get(i).getImei());

                    devs.add(dev);
                }
            }
            bodyMap.put("devs", devs);

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayDevlistResponseBody异常！", e);
        }
        return null;
    }

}
