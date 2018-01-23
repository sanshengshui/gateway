//package com.aiyolo.channel.data.request;
//
//import com.aiyolo.constant.ChannelConsts;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class GatewayMessagePushRequest extends GatewayRequest {
//
//    private static GatewayMessagePushRequest instance;
//
//    public static GatewayMessagePushRequest getInstance() {
//        if (instance == null) {
//            instance = new GatewayMessagePushRequest();
//        }
//        return instance;
//    }
//
//    @Override
//    public Map<String, Object> requestHeader(String[] glIds) {
//        Map<String, Object> headerMap = super.requestHeader(glIds);
//        if (headerMap != null) {
//            headerMap.remove("code");
//            headerMap.put("cmd", "push");
//            headerMap.put("product_id", ChannelConsts.PRODUCT_ID);
//        }
//        return headerMap;
//    }
//
//    @Override
//    public Map<String, Object> requestBody(Map<String, Object> data) {
//        try {
//            Map<String, Object> bodyMap = new HashMap<String, Object>();
//
//            bodyMap.put("gl_id", data.get("glId"));
//            bodyMap.put("title", data.get("title"));
//            bodyMap.put("text", data.get("text"));
//
//            return bodyMap;
//        } catch (Exception e) {
//            errorLogger.error("GatewayMessagePushRequestBody异常！", e);
//        }
//        return null;
//    }
//
//}
