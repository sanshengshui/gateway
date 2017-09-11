package com.aiyolo.channel.data.response;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.constant.AlarmTemperatureConsts;
import com.aiyolo.entity.DeviceSetting;
import net.sf.json.JSONObject;

public class DeviceStatusResponse extends DeviceResponse {

    public static final String ACTION = "status";

    private static DeviceStatusResponse instance;

    public static DeviceStatusResponse getInstance() {
        if (instance == null) {
            instance = new DeviceStatusResponse();
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
            bodyMap.put("res", 0);

            if (data != null) {
                DeviceSetting deviceSetting = (DeviceSetting) data;

                bodyMap.put("tmp1", deviceSetting.getOneLevelAlarmTemperature());
                bodyMap.put("tmp2", deviceSetting.getTwoLevelAlarmTemperature());
            } else {
                bodyMap.put("tmp1", AlarmTemperatureConsts.ONE_LEVEL);
                bodyMap.put("tmp2", AlarmTemperatureConsts.TWO_LEVEL);
            }

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("DeviceStatusResponseBody异常！", e);
        }
        return null;
    }

}
