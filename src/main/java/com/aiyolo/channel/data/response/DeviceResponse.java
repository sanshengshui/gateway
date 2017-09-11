package com.aiyolo.channel.data.response;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ProtocolCodeConsts;

public abstract class DeviceResponse extends Response {

    public Map<String, Object> responseHeader(String glId) {
        String[] glIds = new String[1];
        glIds[0] = glId;
        return responseHeader(glIds);
    }

    @Override
    public Map<String, Object> responseHeader(String[] glIds) {
        try {
            Map<String, Object> headerMap = new HashMap<String, Object>();

            glIds = (String[]) ArrayHelper.removeNullElement(glIds);

            Object[] glIdObjects = new Object[glIds.length];
            for (int i = 0; i < glIds.length; i++) {
                Map<String, String> glIdMap = new HashMap<String, String>();
                glIdMap.put("gl_id", glIds[i]);
                glIdObjects[i] = glIdMap;
            }

            headerMap.put("code", ProtocolCodeConsts.SEND_TO_DEVICE);
            headerMap.put("gl_ids", glIdObjects);
            headerMap.put("cache_time", 0);

            return headerMap;
        } catch (Exception e) {
            errorLogger.error("DeviceResponseHeader异常！", e);
        }
        return null;
    }

}
