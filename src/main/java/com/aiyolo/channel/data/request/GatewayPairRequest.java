package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.ACT;
import static com.aiyolo.constant.ProtocolFieldConsts.IMEI;
import static com.aiyolo.constant.ProtocolFieldConsts.MID;
import static com.aiyolo.constant.ProtocolFieldConsts.PID;
import static com.aiyolo.constant.ProtocolFieldConsts.PIN;

public class GatewayPairRequest extends GatewayRequest {

    public static final String ACTION = "pair";

    private static GatewayPairRequest instance;

    public static GatewayPairRequest getInstance() {
        if (instance == null) {
            instance = new GatewayPairRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        try {
            Map<String, Object> bodyMap = new LinkedHashMap<String, Object>();

            bodyMap.put(ACT, ACTION);
            bodyMap.put(PID, ChannelConsts.PRODUCT_ID);
//            bodyMap.put(PID, product_id);
            bodyMap.put(IMEI, data.get(IMEI));
            bodyMap.put(PIN, data.get(PIN));
            bodyMap.put(MID, System.currentTimeMillis() / 1000);

            return bodyMap;
        } catch (Exception e) {
            errorLogger.error("GatewayPairRequest异常！", e);
        }
        return null;
    }

}
