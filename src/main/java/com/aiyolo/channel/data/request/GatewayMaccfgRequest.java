package com.aiyolo.channel.data.request;

import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.entity.Gateway;

import org.springframework.util.StringUtils;

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
        bodyMap.put(INTV, 0);
        bodyMap.put(LEN, 300);
        return bodyMap;
    }
    public Map<String, Object> requestBody(Gateway gateway) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put(ACT, ACTION);
        bodyMap.put(IMEI, gateway.getGlImei());
        bodyMap.put(PIN, gateway.getGlPin());
        bodyMap.put(PID, ChannelConsts.PRODUCT_ID);
        bodyMap.put(MID, System.currentTimeMillis() / 1000);
        String report_url = gateway.getReport_url();
        Integer report_interval = gateway.getReport_interval();
//        Integer probe_status = gateway.getProbe_status();
//        if (StringUtils.isEmpty(report_url)){
//            report_url = "test.igelian.com";
//        }


        bodyMap.put(SRV, report_url);
        bodyMap.put(PATH, "/gateway_rec/s/gu");
        bodyMap.put(PORT, 9111);
        bodyMap.put(INTV, report_interval);
        bodyMap.put(LEN, 300);
        return bodyMap;
    }

}
