package com.aiyolo.channel.data.request;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.constant.ProtocolCodeConsts;
import com.aiyolo.constant.ProtocolFieldConsts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.aiyolo.constant.ProtocolFieldConsts.CACHE_TIME;
import static com.aiyolo.constant.ProtocolFieldConsts.CODE;
import static com.aiyolo.constant.ProtocolFieldConsts.IMEIS;
import static com.aiyolo.constant.ProtocolFieldConsts.PRODUCT_ID;

public abstract class GatewayRequest extends Request {

    public Map<String, Object> requestHeader(String imei) {
        String[] imeis = new String[1];
        imeis[0] = imei;
        return requestHeader(imeis);
    }

    @Override
    public Map<String, Object> requestHeader(String[] imeis) {
        try {
            Map<String, Object> headerMap = new LinkedHashMap<String, Object>();

            imeis = (String[]) ArrayHelper.removeNullElement(imeis);

//            Object[] glIdObjects = new Object[imeis.length];
//            for (int i = 0; i < imeis.length; i++) {
//                Map<String, String> glIdMap = new HashMap<String, String>();
//                glIdMap.put(ProtocolFieldConsts.IMEI, imeis[i]);
//                glIdObjects[i] = glIdMap;
//            }

            headerMap.put(CODE, ProtocolCodeConsts.SEND_TO_GATEWAY);
            headerMap.put(IMEIS, imeis);
            headerMap.put(CACHE_TIME, 0);
            headerMap.put(PRODUCT_ID, ChannelConsts.PRODUCT_ID);

            return headerMap;
        } catch (Exception e) {
            errorLogger.error("GatewayRequestHeader异常！", e);
        }
        return null;
    }

}
