package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.*;

public class GatewayMaccfgRequest extends GatewayRequest {

    public static final String ACTION = "maccfg";

    private static GatewayMaccfgRequest instance;

    public static GatewayMaccfgRequest getInstance() {
        if (instance == null) {
            instance = new GatewayMaccfgRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put(ACT, ACTION);
        bodyMap.putAll(data);
        bodyMap.put(PID, ChannelConsts.PRODUCT_ID);
        bodyMap.put(MID, System.currentTimeMillis() / 1000);

        bodyMap.put(SRV, "test.igelian.com");
        bodyMap.put(PATH, "/gateway_rec/s/gu");
        bodyMap.put(PORT, 9111);
        bodyMap.put(INTV, 540);
        bodyMap.put(LEN, 300);
        return bodyMap;
    }

}
