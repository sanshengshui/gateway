package com.aiyolo.channel.data.request;

import java.util.HashMap;
import java.util.Map;

import com.aiyolo.common.ArrayHelper;
import com.aiyolo.constant.ChannelConsts;
import com.aiyolo.constant.ProtocolCodeConsts;

import static com.aiyolo.constant.ProtocolFieldConsts.CACHE_TIME;
import static com.aiyolo.constant.ProtocolFieldConsts.CODE;
import static com.aiyolo.constant.ProtocolFieldConsts.IS_TEXT;
import static com.aiyolo.constant.ProtocolFieldConsts.PRODUCT_ID;

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

            headerMap.put(CODE, ProtocolCodeConsts.PUSH_TO_APP);
            headerMap.put("mobile_ids", mobileIdObjects);
            headerMap.put(CACHE_TIME, 0);
            headerMap.put(IS_TEXT, 0);
            headerMap.put(PRODUCT_ID, ChannelConsts.PRODUCT_ID);

            return headerMap;
        } catch (Exception e) {
            errorLogger.error("AppRequestHeader异常！", e);
        }
        return null;
    }

}
