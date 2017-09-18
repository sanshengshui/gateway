package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.entity.Device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatewayDeldevRequest extends GatewayRequest {

    public static final String ACTION = "deldev";

    private static GatewayDeldevRequest instance;

    public static GatewayDeldevRequest getInstance() {
        if (instance == null) {
            instance = new GatewayDeldevRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new HashMap<String, Object>();

            bodyMap.put("act", ACTION);
            bodyMap.put("pid", ChannelConsts.PRODUCT_ID);
            bodyMap.put("imei", data.get("imei"));
            bodyMap.put("pin", data.get("pin"));
            bodyMap.put("mid", System.currentTimeMillis() / 1000);

            List<Map> devs = new ArrayList<Map>();
            if (data.get("devices") != null) {
                List<Device> devices = (List<Device>) data.get("devices");
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
            errorLogger.error("GatewayDeldevRequestBody异常！", e);
        }
        return null;
    }

}
