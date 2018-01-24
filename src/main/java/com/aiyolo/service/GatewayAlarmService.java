package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.constant.AlarmMessageTemplateConsts;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.PushConsts;
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

import java.text.SimpleDateFormat;
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
        Integer lifeValue = AlarmStatusEnum.LIFE.getValue();
        GatewayAlarm gatewayAlarm = new GatewayAlarm(
                "智能网关",
                imei,
                imei,
                mid,
                val,
                lifeValue);

        if (AlarmStatusEnum.CLEAR.getValue().equals(val)) {
            //解除
            // 先查询是否有未解除的报警
            GatewayAlarm _gatewayAlarm = gatewayAlarmRepository.findFirstByImeiAndStatusOrderByIdDesc(
                    imei, lifeValue);
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
                String msgTitle = "智能网关报警";

                long timestamp = gatewayAlarm.getTimestamp() * 1000L;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String address = gatewayService.getFullAddress(gateway.getAddress(), gateway.getAreaCode());
                String contentBase = gateway.getGlName() + "在" + format.format(timestamp);

                String content = contentBase + "监测到" + address + "出现火警，请尽快处理。";
                if (val == 0) {
                    msgTitle = "智能网关解除警报";
                    content = contentBase + "监测发现" + address + "警报已解除。";
                }

                String msgContent = content;
                String smsContent = SmsConsts.SMS_SIGN + "智能网关" + content;

                // 推送给APP
                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", PushConsts.CACHE_TIME);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imeiGateway", imei);
                queryParamMap.put("imei", imei);
                queryParamMap.put("title", msgTitle);
                queryParamMap.put("text", msgContent);
                queryParamMap.put("timestamp", timestamp);

                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                // 推送给个推
                messagePushService.pushMessage(mobileIds, msgTitle, msgContent);

                // 发送短信
                smsPushService.pushSms(imei, smsContent);
            }
        } catch (Exception e) {
            errorLogger.error("pushGatewayAlarm异常！gatewayAlarm:" + gatewayAlarm.toString(), e);
        }
    }


    public void pushChecked(String imei, int mid) {
        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(imei);
        if (gateway == null) {
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String[] mobileIds = gatewayService.getGatewayUserMobileIds(imei);
        // 推送给个推
        messagePushService.pushMessage(mobileIds
                , "巡检通知"
                , "智能网关" + gateway.getGlName()
                        + "在" + format.format(mid * 1000L) + "完成巡检");
    }


}
