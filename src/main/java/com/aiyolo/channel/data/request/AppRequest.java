package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ProtocolCodeConsts;

public abstract class AppRequest extends Request {

    public Map<String, Object> requestHeader(String mobileId) {
        String[] mobileIds = new String[1];
        mobileIds[0] = mobileId;
        return requestHeader(mobileIds);
    }

    @Override
    public Map<String, Object> requestHeader(String[] mobileIds) {
        try {
            Map<String, Object> headerMap = new HashMap<String, Object>();

            mobileIds = (String[]) ArrayHelper.removeNullElement(mobileIds);

            Object[] mobileIdObjects = new Object[mobileIds.length];
            for (int i = 0; i < mobileIds.length; i++) {
                Map<String, String> mobileIdMap = new HashMap<String, String>();
                mobileIdMap.put("mobile_id", mobileIds[i]);
                mobileIdObjects[i] = mobileIdMap;
            }

            headerMap.put("code", ProtocolCodeConsts.SEND_TO_APP);
            headerMap.put("mobile_ids", mobileIdObjects);
            headerMap.put("cache_time", 0);

            return headerMap;
        } catch (Exception e) {
            errorLogger.error("AppRequestHeader异常！", e);
        }
        return null;
    }

}
