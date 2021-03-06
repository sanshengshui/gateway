package com.aiyolo.channel.data.request;

import com.aiyolo.channel.data.processor.GatewayInfoProcessor;
import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.constant.ProtocolCodeConsts;
import com.aiyolo.constant.ProtocolFieldConsts;

import java.util.HashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.CODE;

public class GatewayInfoRequest extends GatewayRequest {

    private static GatewayInfoRequest instance;

    public static GatewayInfoRequest getInstance() {
        if (instance == null) {
            instance = new GatewayInfoRequest();
        }
        return instance;
    }

    @Override
    public Map<String, Object> requestBody(Map<String, Object> data) throws Exception {
        return data;
    }
    //


    @Override
    public Map<String, Object> requestHeader(String imei) {
        Map<String, Object> headerMap = super.requestHeader(imei);
        if (headerMap != null) {
            headerMap.put(CODE, ProtocolCodeConsts.DEVICE_INFO);
            headerMap.remove(ProtocolFieldConsts.IMEIS);


            headerMap.put(ProtocolFieldConsts.IMEI, imei);

            String messageId = generateMessageId();
            GatewayInfoProcessor.MAP_MSG_IMEI.put(messageId, imei);
            headerMap.put(ProtocolFieldConsts.MESSAGE, messageId);

        }
        return headerMap;
    }

//    @Override
//    public Map<String, Object> requestHeader(String[] glIds) {
//        Map<String, Object> headerMap = super.requestHeader(glIds);
//        if (headerMap != null) {
//            headerMap.put(CODE, ProtocolCodeConsts.DEVICE_INFO);
//
//
//        }
//        return headerMap;
//    }
    //
    //    public Map<String, Object> requestBody(String glId) {
    //        Map<String, Object> data = new HashMap<String, Object>();
    //        data.put("glId", glId);
    //        return requestBody(data);
    //    }
    //
    //    @Override
    //    public Map<String, Object> requestBody(Map<String, Object> data) {
    //        try {
    //            Map<String, Object> bodyMap = new HashMap<String, Object>();
    //
    //            bodyMap.put("gl_id", data.get("glId"));
    //
    //            return bodyMap;
    //        } catch (Exception e) {
    //            errorLogger.error("GatewayInfoRequestBody异常！", e);
    //        }
    //        return null;
    //    }

}
