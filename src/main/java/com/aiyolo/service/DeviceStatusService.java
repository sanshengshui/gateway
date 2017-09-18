package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeDeviceRequest;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceStatusService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired Sender sender;

    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceStatusRepository deviceStatusRepository;
    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Autowired GatewayService gatewayService;
    @Autowired PushSettingService pushSettingService;
    @Autowired MessagePushService messagePushService;
    @Autowired SmsPushService smsPushService;

    public void pushDeviceStatus(Device device) {
        pushDeviceStatus(device, AppNoticeTypeConsts.MODIFY);
    }

    public void pushDeviceStatus(Device device, Integer noticeType) {
        pushDeviceStatus(device, noticeType, null);
    }

    public void pushDeviceStatus(Device device, Integer noticeType, DeviceAlarm deviceAlarm) {
        if (device == null) {
            return;
        }

        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(device.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给app
                Map<String, Object> headerMap = AppNoticeDeviceRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imei", device.getImei());
                queryParamMap.put("notice", noticeType);
                queryParamMap.put("dev", device.getType());
                queryParamMap.put("pid", device.getPid());

                DeviceStatus deviceStatus = deviceStatusRepository.findFirstByImeiOrderByIdDesc(device.getImei());
                queryParamMap.put("online", deviceStatus.getOnline());
                queryParamMap.put("location", device.getPosition());
                queryParamMap.put("name", device.getName());
                queryParamMap.put("rssi", deviceStatus.getRssi());
                if (deviceAlarm == null) {
                    DeviceAlarm _deviceAlarm = deviceAlarmRepository.findFirstByImeiOrderByIdDesc(device.getImei());
                    queryParamMap.put("val", _deviceAlarm.getValue());
                } else {
                    queryParamMap.put("val", deviceAlarm.getValue());
                }
                queryParamMap.put("bat", deviceStatus.getBat());

                Map<String, Object> bodyMap = AppNoticeDeviceRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                if (deviceAlarm != null) {
                    // 推送给个推
                    String msgTitle = "";
                    String msgContent = "";
                    String smsContent = "";

                    Map<String, String> placeholderValues = pushSettingService.buildPlaceholderValues(device, deviceAlarm);
                    Map<String, Map<String, String>> pushSetting = pushSettingService.getPushSettingByLevel(
                            pushSettingService.getPushSettingLevel(deviceAlarm.getValue()),
                            placeholderValues);
                    msgTitle = pushSetting.get("app").get("title");
                    msgContent = SmsConsts.SMS_SIGN + pushSetting.get("app").get("content");
                    smsContent = SmsConsts.SMS_SIGN + pushSetting.get("sms").get("content");

                    messagePushService.pushMessage(mobileIds, device.getGateway().getGlId(), msgTitle, msgContent);

                    // 发送短信
                    smsPushService.pushSms(device.getGlImei(), smsContent);
                }
            }
        } catch (Exception e) {
            errorLogger.error("pushDeviceStatus异常！device:" + device.toString() + ", deviceAlarm:" + (deviceAlarm != null ? deviceAlarm.toString() : "null"), e);
        }
    }

    public void pushDeviceStatus(String imei) {
        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
        if (device == null) {
            return;
        }

        pushDeviceStatus(device);
    }

    public void pushDeviceStatus(DeviceAlarm deviceAlarm) {
        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(deviceAlarm.getImei());
        if (device == null) {
            return;
        }

        pushDeviceStatus(device, AppNoticeTypeConsts.MODIFY, deviceAlarm);
    }

}
