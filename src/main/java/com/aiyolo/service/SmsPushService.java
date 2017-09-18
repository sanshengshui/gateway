package com.aiyolo.service;

import com.aiyolo.common.SmsApi;
import com.aiyolo.constant.SmsConsts;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsPushService {

    private static final Log smsLogger = LogFactory.getLog("smsLog");
    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired GatewayService deviceService;

    public void pushSms(String glImei, String text) {
        String[] phones = deviceService.getGatewayUserPhones(glImei);
        if (phones != null && phones.length > 0) {
            pushSms(phones, text);
        }
    }

    public void pushSms(String[] phones, String text) {
        if (phones == null || phones.length == 0 || StringUtils.isEmpty(text)) {
            return;
        }

        try {
            // 发送短信
            String smsMobile = StringUtils.join(phones, ",");
            String smsText = StringUtils.startsWith(text, SmsConsts.SMS_SIGN) ?
                    text : (SmsConsts.SMS_SIGN + text);

            smsLogger.info("发送短信:(" + smsMobile + ")" + smsText);
            String smsResult = SmsApi.sendBatchSms(SmsConsts.API_KEY, smsText, smsMobile);
            smsLogger.info("短信结果:(" + smsMobile + ")" + smsResult);
        } catch (Exception e) {
            errorLogger.error("pushSms异常！text:" + text, e);
        }
    }

}
