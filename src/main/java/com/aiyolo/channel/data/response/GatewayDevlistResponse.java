package com.aiyolo.channel.data.response;

import com.aiyolo.entity.Device;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("pid", request.getJSONObject("body").get("pid"));
            bodyMap.put("imei", request.getJSONObject("body").get("imei"));
            bodyMap.put("pin", request.getJSONObject("body").get("pin"));
            bodyMap.put("mid", request.getJSONObject("body").get("mid"));
            bodyMap.put("ret", 0);

            List<Map> devs = new ArrayList<Map>();
            if (data != null) {
                List<Device> devices = (List<Device>) data;
                for (int i = 0; i < devices.size(); i++) {
                    Map<String, String> dev = new HashMap<String, String>();
                    dev.put("dev", devices.get(i).getType());
                    dev.put("pid", devices.get(i).getPid());
                    dev.put("imei", devices.get(i).getImei());

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
