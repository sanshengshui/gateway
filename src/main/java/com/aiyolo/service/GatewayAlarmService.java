package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.constant.AlarmMessageTemplateConsts;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.entity.Gateway;
import com.aiyolo.entity.GatewayAlarm;
import com.aiyolo.queue.Sender;
//import com.aiyolo.repository.DeviceAlarmCancelRepository;
import com.aiyolo.repository.GatewayAlarmRepository;
import com.aiyolo.repository.GatewayRepository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class GatewayAlarmService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    Sender sender;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    GatewayAlarmRepository gatewayAlarmRepository;

    @Autowired
    GatewayService gatewayService;
    @Autowired
    MessagePushService messagePushService;
    @Autowired
    SmsPushService smsPushService;

    public void gatewayAlarm(String imei, int val, int mid) {
        GatewayAlarm gatewayAlarm = new GatewayAlarm(
                "智能网关",
                imei,
                imei,
                mid,
                val,
                AlarmStatusEnum.LIFE.getValue());

        if (AlarmStatusEnum.CLEAR.getValue().equals(val)) {
            //解除
            // 先查询是否有未解除的报警
            GatewayAlarm _gatewayAlarm = gatewayAlarmRepository.findFirstByImeiAndStatusOrderByIdDesc(
                    imei, AlarmStatusEnum.LIFE.getValue());
            if (_gatewayAlarm == null) {
                // 如果没有未解除的报警，直接返回
                return;
            }
            // 报警解除
            gatewayAlarmRepository.updateStatusByImei(imei, val);

        } else {
            // 添加记录
            gatewayAlarmRepository.save(gatewayAlarm);

        }

        // 推送预报警通知给APP&个推&发送短信
        pushGatewayAlarm(gatewayAlarm, val);

    }

    private void pushGatewayAlarm(GatewayAlarm gatewayAlarm, int val) {
        if (gatewayAlarm == null) {
            return;
        }
        Gateway gateway = gatewayAlarm.getGateway();
        String imei = gatewayAlarm.getGlImei();
        if (gateway == null) {
            gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(imei);
            if (gateway == null) {
                return;
            }

            gatewayAlarm.setGateway(gateway);
        }
        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(imei);
            if (mobileIds != null && mobileIds.length > 0) {
                String msgTitle = "智能网关" + gateway.getGlName() + "报警";
                String content = AlarmMessageTemplateConsts.CONTENT_ALARM;
                if (val == 0) {
                    msgTitle = "智能网关" + gateway.getGlName() + "解除警报";
                    content = AlarmMessageTemplateConsts.CONTENT_CLEAR;
                }
//                String msgContent = "";
//                String smsContent = "";

                //                Map<String, String> placeholderValues = pushSettingService.buildPlaceholderValues(deviceAlarm);
                //                Map<String, Map<String, String>> pushSetting = pushSettingService.getPushSettingByType(
                //                        pushSettingService.getPushSettingType(deviceAlarm.getValue()),
                //                        placeholderValues);
                //                msgTitle = pushSetting.get("app").get("title");

                //                String deviceType = "";
                //                if (StringUtils.isNotEmpty(deviceType)) {
                //                    msgTitle = msgTitle.replace("智能报警器", deviceType);
                //                    msgTitle = msgTitle.replace("警报解除通知", deviceType + "解除警报");
                //                }
                //根据不同报警器设备显示不同的报警标题

                String   msgContent = SmsConsts.SMS_SIGN + content;
                String  smsContent = SmsConsts.SMS_SIGN + content;

                // 推送给APP
                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imeiGateway", imei);
                queryParamMap.put("imei", imei);
                queryParamMap.put("title", msgTitle);
                queryParamMap.put("text", msgContent);
                queryParamMap.put("timestamp", gatewayAlarm.getTimestamp() * 1000L);

                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                // 推送给个推
                messagePushService.pushMessage(mobileIds, gateway.getGlId(), msgTitle, msgContent);

                // 发送短信
                smsPushService.pushSms(imei, smsContent);
            }
        } catch (Exception e) {
            errorLogger.error("pushGatewayAlarm异常！gatewayAlarm:" + gatewayAlarm.toString(), e);
        }
    }


}
