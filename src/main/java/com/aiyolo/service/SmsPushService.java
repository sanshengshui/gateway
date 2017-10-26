package com.aiyolo.service;

import com.aiyolo.common.SmsNewApi;
import com.aiyolo.constant.SmsConsts;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsPushService {

    private static final Log smsLogger = LogFactory.getLog("smsLog");
    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    GatewayService deviceService;

    @Value("${sms.haotingyun.apikey}")
    private String smsHaotingyunApikey;

    @Value("${sms.new.account}")
    private String smsNewAccount;

    @Value("${sms.new.password}")
    private String smsNewPassword;

    @Value("${sms.new.extno}")
    private String smsNewExtno;

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
            String smsResult = SmsNewApi.sendSms(smsNewAccount, smsNewPassword, smsMobile, smsText, smsNewExtno);
            smsLogger.info("短信结果:(" + smsMobile + ")" + smsResult);
        } catch (Exception e) {
            errorLogger.error("pushSms异常！text:" + text, e);
        }
    }

}
