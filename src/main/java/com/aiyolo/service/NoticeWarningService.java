package com.aiyolo.service;

import com.aiyolo.cache.entity.DeviceLatestStatus;
import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NoticeWarningService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");
    private static final Log taskLogger = LogFactory.getLog("taskLog");

    @Autowired Sender sender;

    @Autowired DeviceRepository deviceRepository;

    @Autowired DeviceService deviceService;
    @Autowired DeviceStatusService deviceStatusService;
    @Autowired PushSettingService pushSettingService;
    @Autowired MessagePushService messagePushService;
    @Autowired SmsPushService smsPushService;

    public void pushDeviceUbd(Device device) {
        pushWarningNotice(device, null, false, -1);
    }

    public void pushDeviceStatus(Device device) {
        pushWarningNotice(device, null);
    }

    public void pushWarningNotice(Device device, DeviceAlarm deviceAlarm) {
        pushWarningNotice(device, deviceAlarm, false);
    }

    public void pushWarningNotice(Device device, DeviceAlarm deviceAlarm, Boolean sendSms) {
        pushWarningNotice(device, deviceAlarm, sendSms, null);
    }

    public void pushWarningNotice(Device device, DeviceAlarm deviceAlarm, Boolean sendSms, Integer netStatus) {
        if (device == null) {
            return;
        }

        try {
            String msgTitle = "";
            String msgContent = "";
            String smsContent = "";
            if (deviceAlarm != null) {
                Map<String, String> placeholderValues = pushSettingService.buildPlaceholderValues(device, deviceAlarm);
                Map<String, Map<String, String>> pushSetting = pushSettingService.getPushSettingByLevel(
                        pushSettingService.getPushSettingLevel(deviceAlarm.getType()),
                        placeholderValues);
                msgTitle = pushSetting.get("app").get("title");
                msgContent = SmsConsts.SMS_SIGN + pushSetting.get("app").get("content");
                smsContent = SmsConsts.SMS_SIGN + pushSetting.get("sms").get("content");
            }

            String[] mobileIds = deviceService.getDeviceUserMobileIds(device.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给app
                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imei", device.getGlImei());
                queryParamMap.put("title", msgTitle);
                queryParamMap.put("text", msgContent);

                DeviceLatestStatus latestDeviceStatus = deviceStatusService.getLatestDeviceStatusByGlImei(device.getGlImei());
                queryParamMap.put("alarm_type", deviceAlarm != null ? latestDeviceStatus.getAlarmType() : -1);
                queryParamMap.put("net_status", netStatus != null ? netStatus : latestDeviceStatus.getNetStatus());
                queryParamMap.put("dev_status", latestDeviceStatus.getDevStatus());
                queryParamMap.put("temp", latestDeviceStatus.getTemperature());
                queryParamMap.put("hum", latestDeviceStatus.getHumidity());

                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                if (deviceAlarm != null) {
                    // 推送给个推
                    messagePushService.pushMessage(mobileIds, device.getGlId(), msgTitle, msgContent);
                }
            }

            if (sendSms) {
                // 发送短信
                smsPushService.pushSms(device.getGlImei(), smsContent);
            }
        } catch (Exception e) {
            errorLogger.error("pushWarningNotice异常！device:" + device.toString() + ", deviceAlarm:" + (deviceAlarm != null ? deviceAlarm.toString() : "null"), e);
        }
    }

    public void pushWarningNotice(String glImei, DeviceAlarm deviceAlarm, Boolean sendSms) {
        Device device = deviceRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (device == null) {
            return;
        }

        pushWarningNotice(device, deviceAlarm, sendSms);
    }

    @Async
    public void notifyDeviceLiveStatusChange(String glId) {
        try {
            Device device = deviceRepository.findFirstByGlIdOrderByIdDesc(glId);
            if (device != null) {
                pushDeviceStatus(device);
            }
            taskLogger.info("notifyDeviceLiveStatusChange completed.(glId:" + glId + ")");
        } catch (Exception e) {
            errorLogger.error("notifyDeviceLiveStatusChange异常！glId:" + glId, e);
        }
    }

}
