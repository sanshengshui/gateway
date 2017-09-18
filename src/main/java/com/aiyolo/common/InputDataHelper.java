package com.aiyolo.common;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aiyolo.constant.InputDataTypeEnum;

import net.sf.json.JSONObject;

public class InputDataHelper {

    private static Log errorLogger = LogFactory.getLog("errorLog");

    /**
     * 判断数据类型
     *
     * @param headerJson
     * @param bodyJson
     * @return
     */
    public static InputDataTypeEnum getInputDataType(JSONObject headerJson, JSONObject bodyJson) {
        try {
            if (new Integer(602).equals(headerJson.get("code")) ||
                    new Integer(604).equals(headerJson.get("code")) ||
                    "push".equals(headerJson.get("cmd")) ||
                    "push_text".equals(headerJson.get("cmd"))) {
                return InputDataTypeEnum.CHANNEL_RESPONSE;
            } else if (new Integer(702).equals(headerJson.get("code"))) {
                return InputDataTypeEnum.GATEWAY_BEAT; // 设备心跳
            } else if ("info".equals(headerJson.get("cmd"))) {
                return InputDataTypeEnum.GATEWAY_RESPONSE; // 设备信息查询结果
            } else if ("user_login".equals(headerJson.get("cmd"))) {
                return InputDataTypeEnum.CHANNEL_REQUEST; // 手机登录信息同步
            }

            String action = bodyJson.get("action") != null ? (String) bodyJson.get("action") : (String) bodyJson.get("act");
            for (int i = 0; i < InputDataTypeEnum.values().length; i++) {
                if (ArrayUtils.contains(InputDataTypeEnum.values()[i].getValue(), action)) {
                    return InputDataTypeEnum.values()[i];
                }
            }
        } catch (Exception e) {
            errorLogger.error("判断数据类型异常！", e);
        }
        return null;
    }

}
